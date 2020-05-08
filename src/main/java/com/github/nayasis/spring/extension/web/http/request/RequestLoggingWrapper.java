package com.github.nayasis.spring.extension.web.http.request;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Http request wrapper for logging
 *
 * @author nayasis@gmail.com
 * @since 2015-11-02
 */
@Slf4j
public class RequestLoggingWrapper extends HttpServletRequestWrapper {

    /** Http Request body data **/
    private byte[] body = new byte[] {};

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
            body = IOUtils.toByteArray( inputStream );
        } catch( IOException e ) {
            log.error( e.getMessage(), e );
        }

    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        return new InputStreamWrapper( inputStream );
    }

    public boolean hasInputStream() {
        return body.length > 0;
    }

    /**
     * Servlet input stream implements
     */
    private static class InputStreamWrapper extends ServletInputStream {

        private InputStream inputStream;

        public InputStreamWrapper( InputStream inputStream ) {
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

}