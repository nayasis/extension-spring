package com.github.nayasis.spring.extension.web.client;

import com.github.nayasis.basica.reflection.Reflector;
import com.github.nayasis.spring.extension.web.client.vo.Response;
import com.github.nayasis.spring.extension.web.client.vo.Request;
import com.github.nayasis.basica.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Map;

/**
 * @author nayasis@gmail.com
 * @since 2020-03-17
 */
@Service
@Slf4j
public class RequestSender {

    @Value( "${spring.profiles.active}" )
    private String profiles;

    @Autowired
    private RestOperations restOperations;

    @PostConstruct
    public void init() {
        if( "local".equals(profiles) ) {
            SslChecker.turnOff();
            log.debug( "turn off SSL check." );
        }
    }

    public Response send( Request request ) {
        RequestEntity<Object> requestEntity = toRequestEntity( request );
        try {
            ResponseEntity<String> responseEntity = restOperations.exchange( requestEntity, String.class );
            return toResponse( responseEntity );
        } catch( HttpClientErrorException e ) {
            return toResponse( e );
        } catch( ResourceAccessException e ) {
            return toResponse( e );
        } catch( HttpServerErrorException e ) {
            return toResponse( e );
        }
    }

    private RequestEntity<Object> toRequestEntity( Request request ) {

        Object body       = request.body();
        HttpMethod method = request.method();

        MultiValueMap<String,String> headers = new HttpHeaders();
        if( Validator.isNotEmpty(request.headers()) ) {
            headers.setAll( request.headers() );
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl( request.url() );

        URI uri = uriBuilder.build().encode().toUri();

        return new RequestEntity<>( body, headers, method, uri );

    }

    private Response toResponse( ResponseEntity<String> responseEntity ) {
        Response response = new Response();
        response.status( responseEntity.getStatusCode() );
        response.headers(responseEntity.getHeaders().toSingleValueMap());
        response.responseBody(responseEntity.getBody());
        return response;
    }

    private Response toResponse( ResourceAccessException e ) {
        Response response = new Response();
        response.status( HttpStatus.INTERNAL_SERVER_ERROR );
        response.error( e );
        response.errorDetail( e.getMessage() );
        return response;
    }

    private Response toResponse( HttpClientErrorException exception ) {

        Response response = new Response();
        response.status( exception.getStatusCode() );
        response.headers( exception.getResponseHeaders().toSingleValueMap() );
        response.error( exception );
        response.errorDetail( toErrorDetail( exception.getResponseBodyAsString() ) );

        return response;

    }

    private Response toResponse( HttpServerErrorException exception ) {

        Response response = new Response();
        response.status( exception.getStatusCode() );
        response.headers( exception.getResponseHeaders().toSingleValueMap() );
        response.error( exception );
        response.errorDetail( toErrorDetail( exception.getResponseBodyAsString() ) );

        return response;

    }

    private String toErrorDetail( String errorResponseBody ) {
        try {
            Map<String,Object> errorDetail = Reflector.toMapFrom( errorResponseBody );
            return Reflector.toJson( errorDetail, true );
        } catch( Exception e1 ) {
            return errorResponseBody;
        }
    }

}
