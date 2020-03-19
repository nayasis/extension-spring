package io.nayasis.spring.extension.sql.phase.element.exception;


import io.nayasis.basica.exception.unchecked.BaseRuntimeException;

/**
 * runtimeException when parsing sql expression
 *
 * @author nayasis@gmail.com
 *
 */
public class QueryParseException extends BaseRuntimeException {

    public QueryParseException() {}

    public QueryParseException( Throwable rootCause ) {
        super( rootCause );
    }

    public QueryParseException( String format, Object... parameters ) {
        super( format, parameters );
    }

    public QueryParseException( Throwable rootCause, String format, Object... parameters ) {
        super( rootCause, format, parameters );
    }

}