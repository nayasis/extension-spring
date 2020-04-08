package io.nayasis.spring.extension.sql.phase;

import io.nayasis.basica.reflection.Reflector;
import io.nayasis.spring.extension.sql.expression.NoErrorMapAccessor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

@Slf4j
public class SpringElTest {

    @Test
    public void test() {

        Person p = new Person().name( "nayasis" ).age( 40 ).job( "engineer" );

//        isTrue( "name?.isEmpty()", p );
        isTrue( "${key:}.isEmpty()", p );
        isTrue( "${address}?.isEmpty()", p );
        isTrue( "address == 0", p );

    }

    private boolean isTrue( String expression, Object parameter ) {

        Map param = Reflector.toMapFrom( parameter );

        log.debug( "---------------------------------------------------" );
        log.debug( "expression : {}", expression );
        log.debug( "parameter  : {}", param );

        StandardEvaluationContext context = new StandardEvaluationContext( param );
        context.addPropertyAccessor( new NoErrorMapAccessor() );

        boolean success = toExpression( expression ).getValue( context, boolean.class );

        log.debug( "success    : {}", success );

        return success;
    }

    private Expression toExpression( String expression ) throws ParseException {
        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression( expression );
    }

    @Data @Accessors(fluent=true)
    public static class Person {
        private String name;
        private int    age;
        private String job;
        private String address;
    }


}
