package com.github.nayasis.spring.extension.sql.phase;

import com.github.nayasis.basica.base.Classes;
import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.basica.exception.unchecked.InvalidArgumentException;
import com.github.nayasis.basica.xml.Xml;
import com.github.nayasis.basica.xml.XmlDeformed;
import com.github.nayasis.basica.xml.node.Node;
import com.github.nayasis.spring.extension.sql.phase.node.RootSql;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

@Slf4j
public class SqlReaderTest {

    @Test
    public void readTest() throws MalformedURLException {

        Xml xml = new XmlDeformed( Classes.getResource("/sql/grammar.xml" ) );

        for( Node node : xml.getChildElements( "sql" ) ) {

            String id = node.getAttr( "id" );

            if( Strings.isEmpty(id) )
                throw new InvalidArgumentException( "id is missing" );

            RootSql sql = new SqlMaker().make( "Test", id, node );

            log.debug( ">> {}\n{}", id, sql );

        }

    }

}