package io.nayasis.spring.extension.config.error;

import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import io.nayasis.basica.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nayasis@gmail.com
 * @since 2015-12-03
 */
@Component
@Slf4j
public class Throwables implements ApplicationContextAware {

    @Value( "${server.error.filter:}" )
    private String errorFilter;

    public Throwable filterStackTrace( Throwable throwable ) {

        Throwable clone = new Throwable( throwable.getMessage() );

        List<StackTraceElement> list = new ArrayList<>();

        for( StackTraceElement e : throwable.getStackTrace() ) {
            if( Validator.isFound( e.toString(), errorFilter) ) continue;
            list.add( e );
        }
        clone.setStackTrace( list.toArray( new StackTraceElement[] {} ) );

        if( throwable.getCause() != null ) {
            Throwable cause = filterStackTrace( throwable.getCause() );
            clone.initCause( cause );
        }

        return clone;

    }

    public String toString( Throwable throwable ) {
        if( throwable == null ) return "";
        ThrowableProxy proxy = new ThrowableProxy( filterStackTrace(throwable) );
        proxy.calculatePackagingData();
        return ThrowableProxyUtil.asString( proxy );
    }

    public void logError( Throwable throwable ) {
        Throwable e = filterStackTrace( throwable );
        log.error( e.getMessage(), e );
    }

    @Override
    public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
        errorFilter = errorFilter.replace( "\n", "|" );
    }

}
