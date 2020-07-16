package com.github.nayasis.spring.extension.sql.repository;

import com.github.nayasis.spring.extension.sql.exception.DuplicatedQueryExistException;
import com.github.nayasis.spring.extension.sql.phase.node.ForEachSql;
import com.github.nayasis.spring.extension.sql.phase.node.IfSql;
import com.github.nayasis.spring.extension.sql.phase.node.RootSql;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

        log.debug( "{}", foreach );

    }

}