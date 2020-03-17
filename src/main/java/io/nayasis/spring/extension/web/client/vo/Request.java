package io.nayasis.spring.extension.web.client.vo;

import io.nayasis.basica.base.Strings;
import io.nayasis.basica.reflection.Reflector;
import io.nayasis.spring.extension.web.client.RequestSender;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nayasis@gmail.com
 * @since 2018-03-12
 */
@Data
@Accessors(fluent=true)
@NoArgsConstructor
public class Request {

    private HttpMethod         method = HttpMethod.GET;
    private String             url;
    private Map<String,String> headers;
    private Map<String,Object> params;
    private String             body;
    private RequestSender sender;

    public Request( RequestSender sender ) {
        this.sender = sender;
    }

    public Request url( String url ) {
        this.url = url;
        return this;
    }

    public Request url( String format, Object... param ) {
        this.url = Strings.format( format, param );
        return this;
    }

    public Request jsonBody( Object param ) {
        body = Reflector.toJson( param );
        return this;
    }

    public Request addHeader( String key, String value ) {
        if( headers == null )
            headers = new HashMap<>();
        headers.put( key, value );
        return this;
    }

    public Response send() {
        Response response = sender.send( this );
        response.requestUri( url );
        response.requestBody( body );
        return response;
    }

}
