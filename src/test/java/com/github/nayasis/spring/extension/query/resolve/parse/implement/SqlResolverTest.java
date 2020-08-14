package com.github.nayasis.spring.extension.query.resolve.parse.implement;

import com.github.nayasis.basica.etc.StopWatch;
import com.github.nayasis.spring.extension.query.entity.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SqlResolverTest {

    @Test
    public void basic() {

        StopWatch watch = new StopWatch();

        watch.start( "prepare parameter" ); // this process is slow for the first time because preparing ObjectMapper is expensive.
        QueryParameter param = new QueryParameter( new Person("nayasis",40) );

        watch.start( "parse sql" );
        SqlResolver resolver = new SqlResolver().parse( "SELECT * FROM TABLE WHERE name = #{name} AND age = #{age}", param );

        watch.start( "assert" );
        Assertions.assertEquals( "SELECT * FROM TABLE WHERE name = 'nayasis' AND age = 40", resolver.debugString() );

        watch.stop();

        log.debug( "\n{}", watch );

    }

    @Test
    public void test_01() {

        String sql = "\n            3. SELECT #{user.dept.subDept.name} AS dept FROM DUAL :: #{C}\n        ";

        String modified = replaceKey(sql, "user.dept.subDept", "user.dept.subDept[0]");

        Assertions.assertEquals( "\n            3. SELECT #{user.dept.subDept[0].name} AS dept FROM DUAL :: #{C}\n        ", modified );

    }

    private String replaceKey( String sql, String keyOrigin, String keyReplace ) {

        StringBuilder sb = new StringBuilder();

        Pattern pattern = Pattern.compile( String.format("%s(\\..+?)?", keyOrigin) );

        new SqlResolver().parse(sql, "#{", "}", ( prev, keyword, start, end ) -> {

            Matcher matcher = pattern.matcher(keyword);

            if( matcher.matches() ) {
                sb.append( sql.substring(prev, start) );
                sb.append("#{").append( keyReplace ).append( matcher.replaceFirst("$1") ).append("}");
            } else {
                sb.append( sql.substring(prev,end) );
            }

        }, remain -> {
            sb.append( remain );
        });

        return sb.toString();

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