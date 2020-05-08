package com.github.nayasis.spring.extension.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringExtension implements ApplicationContextAware {

    @Override
    public void setApplicationContext( ApplicationContext context ) throws BeansException {
    }

}
