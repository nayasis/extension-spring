package io.nayasis.basica.spring.web.client.vo;

import io.nayasis.basica.reflection.Reflector;
import io.nayasis.basica.spring.web.client.exception.InvalidResponse;
import io.nayasis.basica.spring.web.client.function.ResponseChecker;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * @author nayasis@gmail.com
 * @since 2018-03-12
 */
@Data
@Accessors(fluent=true)
public class Response {

    private HttpStatus         status;
    private Map<String,String> headers;
    private Exception          error;
    private String             errorDetail;
    private String             requestUri;
    private String             requestBody;
    private String             responseBody;

    public Response responseBody( String body ) {
        requestBody = body;
        return this;
    }

    public String responseBody( HttpStatus... acceptableStatus ) {
        if( ! acceptable(acceptableStatus) )
            throw new InvalidResponse( error, errorDetail, requestUri, requestBody, responseBody );
        return responseBody;
    }

    public <T> T responseBody( Class<T> klass ) {
        return Reflector.toBeanFrom( responseBody, klass );
    }

    public <T> T responseBody( Class<T> klass, HttpStatus... acceptableStatus ) {
        return responseBody( klass, null, acceptableStatus );
    }

    public <T> T responseBody( Class<T> klass, ResponseChecker<T> responseChecker, HttpStatus... acceptableStatus ) {
        if( ! acceptable(acceptableStatus) )
            throw new InvalidResponse( error, errorDetail, requestUri, requestBody, responseBody );
        T response = responseBody( klass );
        try {
            if( responseChecker != null ) {
                responseChecker.check( response );
            }
        } catch ( Exception e ) {
            throw new InvalidResponse( e, e.getMessage(), requestUri, requestBody, responseBody );
        }
        return response;
    }

    private boolean acceptable( HttpStatus... statuses ) {
        if( statuses.length == 0 ) return true;
        for( HttpStatus status : statuses ) {
            if( status == this.status ) return true;
        }
        return false;
    }

}