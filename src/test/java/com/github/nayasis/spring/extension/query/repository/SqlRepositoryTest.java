package com.github.nayasis.spring.extension.query.repository;

import com.github.nayasis.spring.extension.query.entity.QueryParameter;
import com.github.nayasis.spring.extension.query.exception.DuplicatedQueryExistException;
import com.github.nayasis.spring.extension.query.phase.node.ForEachSql;
import com.github.nayasis.spring.extension.query.phase.node.IfSql;
import com.github.nayasis.spring.extension.query.phase.node.RootSql;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SqlRepositoryTest {

    @BeforeAll
    public static void init() throws DuplicatedQueryExistException {
        SqlRepository.readFrom( "/sql/Grammar.xml" );
    }

    @Test
    public void basic() {
        log.debug( "{}", SqlRepository.log() );
    }

    @Test
    public void logForeach() {

        RootSql sql = SqlRepository.get( "Grammar.nestedForLoop" );

        ForEachSql foreach = (ForEachSql) sql.children().stream().filter( n -> n.getClass() == IfSql.class ).findFirst().orElse( null )
            .children().stream().filter( n -> n.getClass() == ForEachSql.class ).findFirst().orElse( null );

        QueryParameter param = new QueryParameter();
        param.put( "user", sampleParam() );

        String query = foreach.toString( param );

        log.debug( ">> query\n{}", query );

    }

    private List<User> sampleParam() {

        List<User> users = new ArrayList<>();

        for( int i=0; i < 2; i++ ) {
            User user = new User().name( "user-" + i ).age(i*20);
            users.add( user );
            for( int j=0; j < 2; j++ ) {
                Dept dept = new Dept().name("dept-"+j);
                user.dept().add( dept );
                for( int k = 0; k < 3; k++ ) {
                    Dept subdept = new Dept().name("subdept-"+k);
                    dept.subDept().add( subdept );
                }
            }
        }

        return users;

    }


    @Data @Accessors(fluent=true)
    class User {
        String     name;
        int        age;
        List<Dept> dept = new ArrayList<>();
    }

    @Data @Accessors(fluent=true)
    class Dept {
        String     name;
        List<Dept> subDept = new ArrayList<>();
    }
    

}