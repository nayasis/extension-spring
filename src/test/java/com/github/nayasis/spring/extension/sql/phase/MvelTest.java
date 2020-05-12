package com.github.nayasis.spring.extension.sql.phase;

import com.github.nayasis.basica.model.NMap;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;

import java.io.Serializable;
import java.util.Map;

@Slf4j
public class MvelTest {

    @Test
    public void test() {

        Person p = testData();

        Map context = new NMap( p );

        VariableResolverFactory functionFactory = new MapVariableResolverFactory( context );

        String expression = " name == 'nayasis' && age == 40 && address == empty";

        Boolean result = (Boolean) MVEL.eval( expression, functionFactory );

        log.debug( "result : {}", result );


    }

    private Person testData() {
        return new Person().name( "nayasis" ).age( 40 ).job( "engineer" );
    }

    @Test
    public void test2() {

        String expression = " name == 'nayasis' && age == 40 && address == empty";

        Serializable compiled = MVEL.compileExpression( expression );

        Object o = MVEL.executeExpression( compiled, testData() );

        log.debug( "result : {}", o );

    }

    @Data
    @Accessors(fluent=true)
    public static class Person {
        private String name;
        private int    age;
        private String job;
        private String address;
    }

}
