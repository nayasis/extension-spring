package com.github.nayasis.spring.extension.sql.phase;

import com.github.nayasis.basica.model.NMap;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;

import java.util.Map;

@Slf4j
public class MvelTest {

    @Test
    public void test() {

        Person p = new Person().name( "nayasis" ).age( 40 ).job( "engineer" );

        Map context = new NMap( p );

        VariableResolverFactory functionFactory = new MapVariableResolverFactory( context );

        String expression = " name == 'nayasis' ";

        Boolean result = (Boolean) MVEL.eval( expression, functionFactory );

        log.debug( "result : {}", result );

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
