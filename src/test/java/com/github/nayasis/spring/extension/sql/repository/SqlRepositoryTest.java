package com.github.nayasis.spring.extension.sql.repository;

import com.github.nayasis.spring.extension.sql.exception.DuplicatedQueryExistException;
import com.github.nayasis.spring.extension.sql.phase.node.RootSql;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

        log.debug( "{}", sql );

    }

}