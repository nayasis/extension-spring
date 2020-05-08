package com.github.nayasis.spring.extension.config.message;

import com.github.nayasis.basica.model.Messages;
import com.github.nayasis.basica.validation.Validator;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

public class CustomMessageSource implements MessageSource {

    private final Object[] EMPTY_ARGS = {};

    @Override
    public String getMessage( String code, Object[] args, String defaultMessage, Locale locale ) {
        return Messages.get( locale, code, Validator.nvl( args, EMPTY_ARGS ) );
    }

    @Override
    public String getMessage( String code, Object[] args, Locale locale ) throws NoSuchMessageException {
        return getMessage( code, args, null, locale );
    }

    @Override
    public String getMessage( MessageSourceResolvable resolvable, Locale locale ) throws NoSuchMessageException {
        String[] codes = resolvable.getCodes();
        Object[] args = Validator.nvl( resolvable.getArguments(), EMPTY_ARGS );
        return Messages.get( locale, codes[0], args );
    }

}
