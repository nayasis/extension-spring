package io.nayasis.spring.extension.web.http;

import com.google.common.net.UrlEscapers;
import io.nayasis.basica.base.Strings;
import io.nayasis.basica.base.Types;
import io.nayasis.spring.extension.web.http.mock.MockHttpServletRequest;
import io.nayasis.spring.extension.web.http.mock.MockHttpServletResponse;
import io.nayasis.basica.thread.local.ThreadRoot;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
@UtilityClass
public class HttpContext {

    private HttpServletRequest  mockRequest  = new MockHttpServletRequest();
    private HttpServletResponse mockResponse = new MockHttpServletResponse();
    private ApplicationContext  context      = null;

    public ApplicationContext ctx() {
        return context;
    }

    public void ctx( ApplicationContext context ) {
        HttpContext.context = context;
    }

    public ServletContext servletContext() {
        return request().getServletContext();
    }

    private ServletRequestAttributes servletAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public HttpServletRequest request() {
        ServletRequestAttributes attributes = servletAttributes();
        return attributes == null ? mockRequest : attributes.getRequest();
    }

    public HttpServletResponse response() {
        ServletRequestAttributes attributes = servletAttributes();
        return attributes == null ? mockResponse : attributes.getResponse();
    }

    public String contextRoot() {
        return request().getContextPath();
    }

    public HttpSession session() {
        return session( false );
    }

    public HttpSession session( boolean create ) {
        return request().getSession( create );
    }

    public Map<String, String> headers() {

        Map<String, String> header = new LinkedHashMap<>();

        HttpServletRequest request = request();

        Enumeration<String> keys = request.getHeaderNames();

        while( keys.hasMoreElements() ) {
            String key = keys.nextElement();
            header.put( key, request.getHeader( key ) );
        }

        return header;

    }

    public String header( String key ) {
        return request().getHeader( key );
    }

    public String userAgent() {
        return header( "user-agent" );
    }

    public Map<String,String> parameters() {

        HttpServletRequest request = servletAttributes().getRequest();

        Map<String, String> parameters = new LinkedHashMap<>();
        Enumeration<String> keys = request.getParameterNames();

        while( keys.hasMoreElements() ) {
            String key = keys.nextElement();
            parameters.put( key, request.getParameter( key ) );
        }

        return parameters;

    }

    public HttpServletResponse setHeaderAsFileDownload( String fileName ) {

        String encodedFileName = escapeAsUrl( fileName );

        HttpServletResponse response = response();

        response.setHeader( "Cache-Control"       , "no-cache, no-store, must-revalidate" );
        response.setHeader( "Pragma"              , "no-cache" );
        response.setHeader( "Expires"             , "0" );
        response.setHeader( "Content-Disposition" , String.format( "attachment;filename=\"%s\"", encodedFileName) );

        return response;

    }

    public String escapeAsUrl( String fileName ) {
        return UrlEscapers.urlFragmentEscaper().escape( fileName );
    }

    /**
     * return Spring bean.
     *
     * @param klass bean class
     * @return Spring bean
     */
    public <T> T bean( Class<T> klass ) {
        return context.getBean( klass );
    }

    /**
     * return Spring bean.
     *
     * @param beanName  bean name
     * @return Spring bean
     */
    public Object bean( String beanName ) {
        return context.getBean( beanName );
    }

    /**
     * SpringBoot environment 설정정보를 가져온다.
     *
     * @return 환경설정정보
     */
    public Environment environment() {
        return HttpContext.bean( Environment.class );
    }

    /**
     * application.properties 파일에 설정된 환경정보를 가져온다.
     *
     * @param key           설정키
     * @param defaultValue  기본값
     * @return 환경정보
     */
    public String property( String key, String defaultValue ) {
        Environment env = environment();
        if( env == null ) return defaultValue;
        return env.getProperty( key, defaultValue );
    }

    /**
     * application.properties 파일에 설정된 환경정보를 가져온다.
     *
     * @param key  설정키
     * @return 환경정보
     */
    public String property( String key ) {
        return property( key, "" );
    }

    /**
     * active profile 설정을 구한다.
     *
     * @return spring.profiles.active
     */
    public String activeProfile() {
        return property( "spring.profiles.active" );
    }

    /**
     * active profile 설정값을 비교한다.
     *
     * @param profile   비교할 profile 값
     * @return 일치여부
     */
    public boolean isActiveProfile( String profile ) {
        return activeProfile().equals( profile );
    }

    /**
     * request를 식별할 수 있는 txId 를 구한다.
     *
     * @return txId 요청 request 식별용 txId
     */
    public String txId() {
        return ThreadRoot.getKey();
    }


    /**
     * 접속한 사용자의 client IP를 구한다.
     * @return 접속한 사용자의 client IP
     */
    public String remoteIp() {
        String address = Strings.nvl( request().getRemoteAddr() );
        return address.replaceAll( ":", "." );
    }

    /**
     * return IP of local host.
     *
     * @return IP
     */
    public String localhostIp() {
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
    public String localhostName() {
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
    public List<Cookie> cookies() {
        return Types.toList( request().getCookies() );
    }

}