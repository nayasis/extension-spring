package io.nayasis.basica.spring.component;

import io.nayasis.basica.cli.CommandExecutor;
import io.nayasis.basica.etc.Platforms;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;
import redis.embedded.exceptions.EmbeddedRedisException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Local Redis Server
 *
 * @author nayasis@gmail.com
 * @since 2016-04-26
 */
@Component
@Slf4j
public class EmbeddedRedis {

    @Value("${spring.redis.embedded.port:6379}")
    private Integer redisPort;

    @Value("${spring.redis.embedded.enable:true}")
    private Boolean enable;

    private RedisServer redisServer = null;

    @PostConstruct
    public void startRedis() {

        if( ! enable ) {
            log.debug( "do not start local redis server because [spring.redis.embedded.enable] in \"application.properties\" is false." );
            return;
        }

        redisServer = new RedisServer( redisPort );

        try {
            redisServer.start();
        } catch( Exception e ) {
            if( Platforms.isWindows ) {
                log.info( "try to kill previous embedded-redis processor." );
                CommandExecutor executor = new CommandExecutor();
                int res = executor.run( "taskkill /F /IM redis-server-2.8.19.exe" ).waitFor( 5_000 );
                if( res == 0 ) {
                    try {
                        redisServer.start();
                    } catch( Exception finalException ) {
                        log.error( finalException.getMessage() );
                    }
                } else {
                    log.error( "fail to kill previous embedded-redis processor." );
                }

            } else {
                log.error( e.getMessage(), e );
            }
        }

    }

    @PreDestroy
    public void stopRedis() {
        if( redisServer != null ) {
            log.debug( ">> Stop local Redis server" );
            try {
                redisServer.stop();
            } catch ( EmbeddedRedisException e ) {
                log.error( e.getMessage(), e );
            }
        }
    }

}
