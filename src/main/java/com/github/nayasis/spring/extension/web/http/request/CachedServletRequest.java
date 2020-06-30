package com.github.nayasis.spring.extension.web.http.request;

import com.github.nayasis.basica.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * Http request wrapper caching contents to read inputstream multiple.
 *
 * @author nayasis@gmail.com
 * @since 2015-11-02
 */
@Slf4j
public class CachedServletRequest extends HttpServletRequestWrapper {

    private byte[]               body;
    private Map<String,String[]> params;

    /**
     * Constructs wrapping the given request.
     *
     * @param request request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public CachedServletRequest( HttpServletRequest request ) {

        super( request );

        params = Collections.unmodifiableMap( request.getParameterMap() );

        try {
            body = IOUtils.toByteArray( request.getInputStream() );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }

    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new InputStreamWrapper( body );
    }

    @Override
    public String getParameter( String name ) {
        String[] values = getParameterValues( name );
        if( values == null || values.length == 0 ) {
            return null;
        } else {
            return values[0];
        }
    }

    @Override
    public Map<String,String[]> getParameterMap() {
        return params;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    @Override
    public String[] getParameterValues( String name ) {
        String[] trg = null;
        String[] src = params.get( name );
        if ( src != null ) {
            trg = new String[ src.length ];
            System.arraycopy( src, 0, trg, 0, src.length );
        }
        return trg;
    }

    @Override
    public BufferedReader getReader() throws IOException{
        return new BufferedReader( new InputStreamReader(getInputStream()) );
    }

    public boolean hasContentType( MediaType... type ) {
        String contentType = Strings.toLowerCase(getContentType());
        if( contentType.isEmpty() ) return false;
        for( MediaType t : type ) {
            if( contentType.contains(t.getType()) ) return true;
        }
        return false;
    }

    /**
     * Servlet inputstream wrapper
     */
    private static class InputStreamWrapper extends ServletInputStream {

        private ByteArrayInputStream input;

        public InputStreamWrapper( byte[] body ) {
            this.input = new ByteArrayInputStream( body );
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
            return input.read();
        }

    }

}