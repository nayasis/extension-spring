package com.github.nayasis.spring.extension.sql.sqlMaker;

import com.github.nayasis.basica.base.format.Formatter;
import com.github.nayasis.spring.extension.sql.entity.QueryParameter;
import lombok.experimental.UtilityClass;

import static com.github.nayasis.basica.base.format.Formatter.PATTERN_DOLLAR;
import static com.github.nayasis.basica.base.format.Formatter.PATTERN_SHARP;
import static com.github.nayasis.spring.extension.sql.common.QueryConstant.PARAMETER_SINGLE;

@UtilityClass
public class QueryResolver {

    public String bindDynamicQuery( String query, QueryParameter parameter ) {
        return new Formatter().bindParam( PATTERN_DOLLAR, query, parameter, ( key, format, param ) -> {
            if( ! param.containsKey(key) ) return String.format( "${%s}", key );
            return param.getByJsonPath( key );
        }, false );
    }

    public String bindSingleParameterKey( String query, QueryParameter parameter ) {
        return new Formatter().bindParam( PATTERN_SHARP, query, parameter, (key,format,param) -> {
            if( ! param.containsJsonPath(key) ) return String.format( "#{%s}", PARAMETER_SINGLE );
            return String.format( "#{%s}", key );
        }, false );
    }

}
