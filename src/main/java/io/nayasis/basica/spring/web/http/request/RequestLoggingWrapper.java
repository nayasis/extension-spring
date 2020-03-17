package io.nayasis.basica.spring.web.http.request;

import io.nayasis.basica.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Http request wrapper for logging
 *
 * @author nayasis@gmail.com
 * @since 2015-11-02
 */
@Slf4j
public class RequestLoggingWrapper extends HttpServletRequestWrapper {

    /** Http Request body data **/
    private byte[] requestBody = new byte[] {};

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public RequestLoggingWrapper( HttpServletRequest request ) {

        super( request );

        try {

            ServletInputStream inputStream = super.getInputStream();
            requestBody = IOUtils.toByteArray( inputStream );

        } catch( IOException e ) {
            log.error( e.getMessage(), e );
        }

    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( requestBody );
        return new ServletInputStreamImpl( byteArrayInputStream );
    }

    public boolean hasInputStream() {
        return requestBody.length > 0;
    }

    /**
     * Servlet input stream implements
     */
    private static class ServletInputStreamImpl extends ServletInputStream {

        private InputStream inputStream;

        public ServletInputStreamImpl( InputStream inputStream ) {
            this.inputStream = inputStream;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener( ReadListener listener ) {}

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

    }

    /**
     * XSS(Cross Site Script) 보안처리가 적용된 메서드
     *
     * @param name 파라미터명
     * @return XSS 처리된 파라미터값
     */
    @Override
    public String getParameter( String name ) {
        return clearXss( super.getParameter(name) );
    }

    /**
     * XSS(Cross Site Script) 보안처리가 적용된 메서드
     *
     *
     * @param name 파라미터명
     * @return XSS 처리된 파라미터값 목록
     */
    @Override
    public String[] getParameterValues( String name ) {
        String[] values = super.getParameterValues( name );
        clearXss( values );
        return values;
    }

    /**
     * XSS(Cross Site Script) 보안처리가 적용된 메서드
     *
     * @return XSS 처리된 파라미터값을 담고 있는 Map
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        for( String[] values : map.values() ) {
            clearXss( values );
        }
        return map;
    }

    private void clearXss( String[] values ) {
        if( values != null ) {
            for( int i = 0, iCnt = values.length; i < iCnt; i++ ) {
                values[ i ] = clearXss( values[i] );
            }
        }
    }

    private String clearXss( String value ) {
        if( value == null ) return value;
        return Strings.clearXss( value );
    }

}
