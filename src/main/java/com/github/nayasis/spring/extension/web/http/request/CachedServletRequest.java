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

/**
 * Http request wrapper caching contents to read inputstream multiple.
 *
 * @author nayasis@gmail.com
 * @since 2015-11-02
 */
@Slf4j
public class CachedServletRequest extends HttpServletRequestWrapper {

    private byte[] cachedBody = null;

    /**
     * Constructs wrapping the given request.
     *
     * @param request request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public CachedServletRequest( HttpServletRequest request ) {
        super( request );
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if( cachedBody == null ) {
            // do read parameter map if request is form type parameter.
            if( getContentType().contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE) ) {
                super.getParameterNames();
            }
            cachedBody = IOUtils.toByteArray( super.getInputStream() );
        }
        return new InputStreamWrapper( cachedBody );
    }

    @Override
    public BufferedReader getReader() throws IOException{
        return new BufferedReader( new InputStreamReader(getInputStream()) );
    }

    public boolean isContentType( String... type ) {
        String contentType = Strings.toLowerCase(getContentType());
        if( contentType.isEmpty() ) return false;
        for( String t : type ) {
            if( contentType.contains(t) ) return true;
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