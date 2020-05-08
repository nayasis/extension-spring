package com.github.nayasis.spring.extension.sql.expression;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.TypedValue;

@Slf4j
public class NoErrorMapAccessor extends MapAccessor {

    @Override
    public boolean canRead( EvaluationContext context, Object target, String name ) throws AccessException {
        return true;
    }

    @Override
    public TypedValue read( EvaluationContext context, Object target, String name ) throws AccessException {
        try {
            return super.read( context, target, name );
        } catch ( Exception e ) {
            return new TypedValue( null );
        }
    }
}
