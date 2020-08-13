package com.github.nayasis.spring.extension.query.resolve.parse.implement;

import com.github.nayasis.spring.extension.query.entity.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class SqlResolverTest {

    @Test
    public void basic() {

        QueryParameter param = new QueryParameter( new Person("nayasis",40) );

        SqlResolver resolver = new SqlResolver().parse( "SELECT * FROM TABLE WHERE name = #{name} AND age = #{age}", param );

        Assertions.assertEquals( "SELECT * FROM TABLE WHERE name = 'nayasis' AND age = 40", resolver.debugString() );

    }


    @Data @Accessors(fluent=true)
    class Person {

        private String name;
        private int    age;

        public Person( String name, int age ) {
            this.name = name;
            this.age = age;
        }

    }

}