package com.github.nayasis.spring.extension.web.http;

import com.google.common.net.UrlEscapers;
import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.basica.base.Types;
import com.github.nayasis.basica.thread.local.ThreadRoot;
import com.github.nayasis.spring.extension.web.http.mock.MockHttpServletRequest;
import com.github.nayasis.spring.extension.web.http.mock.MockHttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Http Context
 *
 * @author nayasis@gmail.com
 * @since 2015-08-28
 */
@Component("httpctx")
public class HttpContext implements ApplicationContextAware {

    private static HttpServletRequest  mockRequest  = new MockHttpServletRequest();
    private static HttpServletResponse mockResponse = new MockHttpServletResponse();
    private static ApplicationContext  context      = null;

    public static ApplicationContext ctx() {
        return context;
    }

    public static void ctx( ApplicationContext context ) {
        HttpContext.context = context;
    }

    public static ServletContext servletContext() {
        return request().getServletContext();
    }

    private static ServletRequestAttributes servletAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletRequest request() {
        ServletRequestAttributes attributes = servletAttributes();
        return attributes == null ? mockRequest : attributes.getRequest();
    }

    public static HttpServletResponse response() {
        ServletRequestAttributes attributes = servletAttributes();
        return attributes == null ? mockResponse : attributes.getResponse();
    }

    public static boolean hasContentType( MediaType... type ) {
        String contentType = getContentType();
        for( MediaType t : type ) {
            if( contentType.contains(t.getType()) ) return true;
        }
        return false;
    }

    public static String getContentType() {
        return Strings.toLowerCase( request().getContentType() );
    }

    public static String contextRoot() {
        return request().getContextPath();
    }

    public static HttpSession session() {
        return session( false );
    }

    public static HttpSession session( boolean create ) {
        return request().getSession( create );
    }

    public static Map<String,String> headers() {

        Map<String,String> header = new LinkedHashMap<>();

        HttpServletRequest request = request();

        Enumeration<String> keys = request.getHeaderNames();

        while( keys != null && keys.hasMoreElements() ) {
            String key = keys.nextElement();
            header.put( key, request.getHeader( key ) );
        }

        return header;

    }

    public static String header( String key ) {
        return request().getHeader( key );
    }

    public static String userAgent() {
        return header( "user-agent" );
    }

    public static Map<String,String> parameters() {

        HttpServletRequest request = servletAttributes().getRequest();

        Map<String, String> parameters = new LinkedHashMap<>();
        Enumeration<String> keys = request.getParameterNames();

        while( keys != null && keys.hasMoreElements() ) {
            String key = keys.nextElement();
            parameters.put( key, request.getParameter( key ) );
        }

        return parameters;

    }

    public static HttpServletResponse setHeaderAsFileDownload( String fileName ) {

        String encodedFileName = escapeAsUrl( fileName );

        HttpServletResponse response = response();

        response.setHeader( "Cache-Control"       , "no-cache, no-store, must-revalidate" );
        response.setHeader( "Pragma"              , "no-cache" );
        response.setHeader( "Expires"             , "0" );
        response.setHeader( "Content-Disposition" , String.format( "attachment;filename=\"%s\"", encodedFileName) );

        return response;

    }

    public static String escapeAsUrl( String fileName ) {
        return UrlEscapers.urlFragmentEscaper().escape( fileName );
    }

    /**
     * return Spring bean.
     *
     * @param klass bean class
     * @param <T> return type of bean
     * @return Spring bean
     */
    public static <T> T bean( Class<T> klass ) {
        return context.getBean( klass );
    }

    /**
     * return Spring bean.
     *
     * @param beanName  bean name
     * @param <T> return type of bean
     * @return Spring bean
     */
    public static <T> T bean( String beanName ) {
        Object bean = context.getBean(beanName);
        return bean == null ? null : (T) bean;
    }

    /**
     * return SpringBoot environment bean
     *
     * @return environment bean
     */
    public static Environment environment() {
        return HttpContext.bean( Environment.class );
    }

    /**
     * return configuration value in application.properties(or yml)
     *
     * @param key           configuration key
     * @param defaultValue  default value
     * @return configuration value
     */
    public static String environment( String key, String defaultValue ) {
        Environment env = environment();
        if( env == null ) return defaultValue;
        return env.getProperty( key, defaultValue );
    }

    /**
     * return configuration value in application.properties(or yml)
     *
     * @param key  configuration key
     * @return configuration value
     */
    public static String environment( String key ) {
        return environment( key, "" );
    }

    /**
     * return active profile value.
     *
     * @return spring.profiles.active
     */
    public static String activeProfile() {
        return environment( "spring.profiles.active" );
    }

    /**
     * check if specific profile is active.
     *
     * @param profile   comparable profile.
     * @return true if given profile is active
     */
    public static boolean hasProfile( String profile ) {
        return activeProfile().contains( profile );
    }

    /**
     * return transaction ID based on UUID.
     *
     * @return transaction ID
     */
    public static String txId() {
        return ThreadRoot.getKey();
    }

    /**
     * return IP of remote client user.
     *
     * @return client's IP
     */
    public static String remoteIp() {
        String address = Strings.nvl( request().getRemoteAddr() );
        return address.replaceAll( ":", "." );
    }

    /**
     * return IP of local host.
     *
     * @return IP
     */
    public static String localhostIp() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return Strings.nvl( ip.getHostAddress() ).replaceAll( ":", "." );
        } catch( UnknownHostException e ) {
            return "";
        }
    }

    /**
     * return hostname of local host.
     *
     * @return hostname
     */
    public static String localhostName() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return Strings.nvl( ip.getHostName() );
        } catch( UnknownHostException e ) {
            return "";
        }
    }

    /**
     * return HTTP cookies.
     *
     * @return HTTP cookies.
     */
    public static List<Cookie> cookies() {
        return Types.toList( request().getCookies() );
    }

    @Override
    public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
        this.context = applicationContext;
    }

}