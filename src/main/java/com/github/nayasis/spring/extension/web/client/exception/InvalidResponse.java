package com.github.nayasis.spring.extension.web.client.exception;

import com.github.nayasis.basica.base.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidResponse extends ResponseStatusException {

    public InvalidResponse( String errorMessage ) {
        super( HttpStatus.INTERNAL_SERVER_ERROR, errorMessage );
    }

    public InvalidResponse( Exception exception, String errorDetail, String requestUri, String requestBody, String responseBody ) {
        super(
            HttpStatus.INTERNAL_SERVER_ERROR,
            Strings.format("{}\n\n{}\n\n- Request URI : {} \n- Request Body:\n {} \n- Response Body:\n {}",
                exception.getMessage(), errorDetail, requestUri, requestBody, responseBody) );
            setStackTrace( exception.getStackTrace()
            );
    }

}