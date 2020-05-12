package com.github.nayasis.spring.extension.sql.exception;

import com.github.nayasis.basica.exception.unchecked.BaseRuntimeException;

public class QueryParseException extends BaseRuntimeException {

    public QueryParseException( String format, Object... parameters ) {
        super( format, parameters );
    }

}
