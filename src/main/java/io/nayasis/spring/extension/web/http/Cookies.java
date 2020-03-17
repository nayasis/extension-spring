package io.nayasis.spring.extension.web.http;

import io.nayasis.basica.validation.Validator;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

@Service
public class Cookies {

    public Map<String,Cookie> getAll() {

        Map<String,Cookie> map = new HashMap<>();

        Cookie[] cookies = HttpContext.request().getCookies();

        if(Validator.isNotEmpty(cookies) ) {
            for( int i = 0; i < cookies.length; i++ ) {
                map.put( cookies[i].getName(), cookies[i] );
            }
        }

        return map;

    }

    public boolean exists( String name ) {
        return getAll().containsKey( name );
    }

    public Cookie get( String name ) {
        return getAll().get( name );
    }

    public Cookie create( String name, String value ) {
        Cookie cookie = new Cookie( name, value );
        return cookie;
    }

    public Cookie create( String name, String value, String path, int maxAge ) {
        Cookie cookie = create( name, value );
        cookie.setPath( path );
        cookie.setMaxAge( maxAge );
        return cookie;
    }

}
