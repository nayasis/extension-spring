package io.nayasis.spring.extension.config.shutdown.graceful.undertow;

import io.undertow.server.handlers.GracefulShutdownHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GracefulShutdownEventListener implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private GracefulShutdownHandlerWrapper gracefulShutdownHandlerWrapper;

    @Override
    public void onApplicationEvent( ContextClosedEvent event ) {

        GracefulShutdownHandler shutdownHandler = gracefulShutdownHandlerWrapper.getGracefulShutdownHandler();

        if( shutdownHandler == null ) return;

        log.info( "graceful shutdown started." );

        // 신규 request 인입 거부 (503 service unavailable 반환)
        shutdownHandler.shutdown();

        try {
            // 기존 처리중인 request 완료대기
            shutdownHandler.awaitShutdown();
        } catch ( Exception ex ) {
            ex.printStackTrace( System.err );
            log.error("graceful shutdown failed.");
        }

        log.info( "graceful shutdown finished." );

    }

}
