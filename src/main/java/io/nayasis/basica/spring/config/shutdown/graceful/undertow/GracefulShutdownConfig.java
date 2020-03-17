package io.nayasis.basica.spring.config.shutdown.graceful.undertow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GracefulShutdownConfig {

    @Autowired
    private GracefulShutdownHandlerWrapper gracefulShutdownHandlerWrapper;

    @Bean
    public UndertowServletWebServerFactory servletWebServerFactory() {

        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.addDeploymentInfoCustomizers( deploymentInfo -> {
            deploymentInfo.addOuterHandlerChainWrapper( gracefulShutdownHandlerWrapper );
        } );
        return factory;

    }

}
