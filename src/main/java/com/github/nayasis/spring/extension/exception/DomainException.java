package com.github.nayasis.spring.extension.exception;

import com.github.nayasis.basica.exception.unchecked.BaseRuntimeException;

public class DomainException extends BaseRuntimeException {

    private static final long serialVersionUID = 4104745246062354093L;

    /**
     * constructor
     */
    public DomainException() {
        super();
    }

    /**
     * constructor
     *
     * @param errorCode  error code ( or message code )
     * @param parameter  substitutable parameters for '{}' pattern in message code
     */
    public DomainException( String errorCode, Object... parameter ) {
        super( errorCode, parameter );
        super.errorCode( errorCode );
    }

    /**
     * constructor
     *
     * @param rootCause  root cause
     * @param errorCode  error code ( or message code )
     * @param parameter  substitutable parameters for '{}' pattern in message code
     */
    public DomainException( Throwable rootCause, String errorCode, Object... parameter ) {
        super( rootCause, errorCode, parameter );
        super.errorCode( errorCode );
    }

    /**
     * constructor
     *
     * @param rootCause root cause
     */
    public DomainException( Throwable rootCause ) {
        super( rootCause );
    }

}