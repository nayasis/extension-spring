package io.nayasis.basica.spring.web.client.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept( HttpRequest request, byte[] body, ClientHttpRequestExecution execution ) throws IOException {
        traceRequest( request, body );
        ClientHttpResponse response = execution.execute( request, body );
        traceResponse( request, response );
        return response;
    }

    private void traceRequest( HttpRequest request, byte[] body ) {

        if( ! log.isTraceEnabled() ) return;

        StringBuilder sb = new StringBuilder();
        sb.append( "[REQUEST]\n" )
            .append( String.format( "- URI : %s\n", toUri( request ) ) )
            .append( "- Body\n" ).append( new String(body, UTF_8 ) );

        log.trace( sb.toString() );

    }

    private String toUri( HttpRequest request ) {
        return String.format("[%s] %s", request.getMethod(), request.getURI());
    }

    private void traceResponse( HttpRequest request, ClientHttpResponse response ) throws IOException {

        if( ! log.isTraceEnabled() ) return;

        StringBuilder sb = new StringBuilder();
        sb.append( "[RESPONSE]\n" )
            .append( String.format( "- URI : %s\n", toUri( request ) ) )
            .append( "- Status : " ).append( response.getStatusCode() ).append( '\n' )
            .append( "- Body\n" ).append( StreamUtils.copyToString( response.getBody(), UTF_8 ) );

        log.trace( sb.toString() );

    }

}
