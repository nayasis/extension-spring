package com.github.nayasis.spring.extension.cache;

import com.github.nayasis.basica.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.springframework.data.redis.core.ScanOptions.scanOptions;

/**
 * Redis cache writer to clear all keys on production redis cluster.
 *
 * @since  2020-06-23
 */
@Slf4j
public class ClearAllRedisCacheWriter implements RedisCacheWriter {

    private final RedisConnectionFactory connectionFactory;
    private final Duration               sleepTime;

    private static Set<String> CACHE_NAME_ON_CLEANING = ConcurrentHashMap.newKeySet();

    public ClearAllRedisCacheWriter( RedisConnectionFactory connectionFactory ) {
        this(connectionFactory, Duration.ZERO);
    }

    public ClearAllRedisCacheWriter( RedisConnectionFactory connectionFactory, Duration sleepTime ) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        Assert.notNull(sleepTime, "SleepTime must not be null!");

        this.connectionFactory = connectionFactory;
        this.sleepTime = sleepTime;

    }

    @Override
    public void put( String name, byte[] key, byte[] value, @Nullable Duration ttl ) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        execute(name, connection -> {
            if (shouldExpireWithin(ttl)) {
                connection.set(key, value, Expiration.from(ttl.toMillis(), TimeUnit.MILLISECONDS), RedisStringCommands.SetOption.upsert());
            } else {
                connection.set(key, value);
            }
            return "OK";
        });
    }

    @Override
    public byte[] get(String name, byte[] key) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");

        return execute(name, connection -> connection.get(key));
    }

    @Override
    public byte[] putIfAbsent( String name, byte[] key, byte[] value, @Nullable Duration ttl ) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        return execute(name, connection -> {
            if ( isLockingCacheWriter() )
                lock( name, connection );
            try {
                if (connection.setNX(key, value)) {
                    if (shouldExpireWithin(ttl)) {
                        connection.pExpire(key, ttl.toMillis());
                    }
                    return null;
                }
                return connection.get(key);
            } finally {
                if (isLockingCacheWriter()) {
                    unlock(name, connection);
                }
            }
        });

    }

    @Override
    public void remove(String name, byte[] key) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");

        execute(name, connection -> connection.del(key));
    }

    @Override
    public void clean(String name, byte[] pattern) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(pattern, "Pattern must not be null!");

        execute(name, connection -> {

            if( CACHE_NAME_ON_CLEANING.contains(name) ) {
                log.error( "cancel cache cleaning because task({}) is already running.", name );
                return "DONE";
            }

            CACHE_NAME_ON_CLEANING.add(name);

            long        total = 0L;
            Set<byte[]> keys  = new HashSet();

            try {

                ScanOptions condition = scanOptions().count( 1000L ).match( new String(pattern) ).build();

                Cursor cursor = connection.scan( condition );

                while( cursor.hasNext() ) {
                    byte[] value = (byte[]) cursor.next();
                    if ( value.length > 0 ) {
                        keys.add(value);
                        total++;
                    }
                    if ( total % 100 == 0 )
                        clean( keys, connection );
                }

            } finally {
                clean( keys, connection );
                CACHE_NAME_ON_CLEANING.remove(name);
                log.info( "clean (key:{}, count:{})", name, total );
            }

            return "OK";

        });

    }

    private void clean( Set<byte[]> keys, RedisConnection connection ) {
        if( Validator.isEmpty(keys) ) return;
        try {
            connection.del( keys.toArray(new byte[keys.size()][]) );
            keys.clear();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    private Boolean lock( String name, RedisConnection connection ) {
        return connection.setNX( getLockKey(name), new byte[0]);
    }

    private Long unlock( String name, RedisConnection connection ) {
        return connection.del(getLockKey(name));
    }

    private boolean isLocked( String name, RedisConnection connection ) {
        return connection.exists( getLockKey(name));
    }

    private boolean isLockingCacheWriter() {
        return ! sleepTime.isZero() && ! sleepTime.isNegative();
    }

    private <T> T execute( String name, Function<RedisConnection,T> callback ) {
        RedisConnection connection = connectionFactory.getConnection();
        try {
            waitUnlocked(name, connection);
            return callback.apply(connection);
        } finally {
            connection.close();
        }
    }

    private void waitUnlocked(String name, RedisConnection connection) {
        if ( ! isLockingCacheWriter() ) return;
        try {
            while ( isLocked(name, connection)) {
                Thread.sleep(sleepTime.toMillis());
            }
        } catch (InterruptedException ex) {
            // Re-interrupt current thread, to allow other participants to react.
            Thread.currentThread().interrupt();
            throw new PessimisticLockingFailureException( String.format("Interrupted while waiting to unlock cache %s", name), ex );
        }
    }

    private boolean shouldExpireWithin( Duration ttl ) {
        return ttl != null && !ttl.isZero() && !ttl.isNegative();
    }

    private byte[] getLockKey( String name ) {
        return (name + "~lock").getBytes( StandardCharsets.UTF_8 );
    }

}
