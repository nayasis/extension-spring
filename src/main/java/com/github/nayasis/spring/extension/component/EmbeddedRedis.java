package com.github.nayasis.spring.extension.component;

import com.github.nayasis.basica.cli.CommandExecutor;
import com.github.nayasis.basica.etc.Platforms;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;
import redis.embedded.exceptions.EmbeddedRedisException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Local Redis Server
 *
 * @author nayasis@gmail.com
 * @since 2020-03-17
 */
@Component
@ConditionalOnClass( RedisServer.class )
@Slf4j
public class EmbeddedRedis {

    @Value("${spring.redis.embedded.port:6379}")
    private Integer redisPort;

    @Value("${spring.redis.embedded.enable:false}")
    private Boolean enable;

    private RedisServer redisServer = null;

    @PostConstruct
    public void startRedis() {

        if( ! enable ) {
            log.debug( "DO NOT start local redis server because [spring.redis.embedded.enable] in \"application.properties(or yml)\" is false." );
            return;
        }

        redisServer = new RedisServer( redisPort );

        try {
            redisServer.start();
            logSuccess();
        } catch( Exception e ) {
            log.info( "try to kill previous embedded-redis processor." );
            if( killProcess() ) {
                try {
                    redisServer.start();
                    logSuccess();
                    return;
                } catch( Exception ex2nd ) {
                    log.error( "fail to kill previous embedded-redis processor.", ex2nd );
                }
            } else {
                log.error( "fail to kill previous embedded-redis processor." );
            }
        }

    }

    private boolean killProcess() {
        String command = getKillCommand();
        if( command == null ) return false;
        return new CommandExecutor().run( command ).waitFor( 5_000 ) == 0;
    }

    private String getKillCommand() {
        String cmd = null;
        if( Platforms.isWindows ) {
            cmd = "taskkill /F /IM redis-server-2.8.19.exe";
        }
        return cmd;
    }

    private void logSuccess() {
        log.debug( "embedded redis started (port:{})", redisPort );
    }

    @PreDestroy
    public void stopRedis() {
        if( redisServer != null ) {
            log.debug( "stop local Redis server" );
            try {
                redisServer.stop();
            } catch ( EmbeddedRedisException e ) {
                log.error( e.getMessage(), e );
            }
        }
    }

}