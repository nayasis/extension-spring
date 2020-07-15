package com.github.nayasis.spring.extension.sql.exception;

import com.github.nayasis.basica.exception.unchecked.BaseException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicatedQueryExistException extends BaseException {

    public DuplicatedQueryExistException( Throwable cause ) {
        super( cause );
    }

    public DuplicatedQueryExistException( String format, Object... parameters ) {
        super( format, parameters );
    }

    public DuplicatedQueryExistException( Throwable cause, String format, Object... parameters ) {
        super( cause, format, parameters );
    }

}