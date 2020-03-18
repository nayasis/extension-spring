package io.nayasis.spring.extension.config.shutdown.graceful.undertow;

import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GracefulShutdownConfig {

    private GracefulShutdownHandlerWrapper handlerWrapper = null;

    public void setGracefulShutdownHandlerWrapper( GracefulShutdownHandlerWrapper gracefulShutdownHandlerWrapper ) {
        this.handlerWrapper = gracefulShutdownHandlerWrapper;
    }

    @Bean
    public UndertowServletWebServerFactory servletWebServerFactory() {
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.addDeploymentInfoCustomizers( deploymentInfo -> {
            deploymentInfo.addOuterHandlerChainWrapper(handlerWrapper);
        });
        return factory;
    }

}
