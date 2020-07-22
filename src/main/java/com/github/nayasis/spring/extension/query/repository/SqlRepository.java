package com.github.nayasis.spring.extension.query.repository;

import com.github.nayasis.basica.base.Classes;
import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.basica.file.Files;
import com.github.nayasis.basica.validation.Assert;
import com.github.nayasis.basica.xml.Xml;
import com.github.nayasis.basica.xml.XmlDeformed;
import com.github.nayasis.basica.xml.node.Node;
import com.github.nayasis.spring.extension.query.exception.DuplicatedQueryExistException;
import com.github.nayasis.spring.extension.query.phase.SqlMaker;
import com.github.nayasis.spring.extension.query.phase.node.RootSql;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@UtilityClass
public class SqlRepository {

    private Map<String,RootSql> repository;

    private String LINE;

    static {
        repository = new HashMap<>();
        LINE = Strings.line( '-', 50 );
    }

    public boolean contains( String sqlId ) {
        return repository.containsKey( sqlId );
    }

    public RootSql get( String sqlId ) {
        return repository.get( sqlId );
    }

    public RootSql remove( String sqlId ) {
        return repository.remove( sqlId );
    }

    public void put( RootSql sql ) throws DuplicatedQueryExistException {
        if( contains(sql.id()) )
            throw new DuplicatedQueryExistException( "SQL ID({}) is duplicated", sql.id() );
        repository.put( sql.id(), sql );
    }

    public Map<String,RootSql> repository() {
        return repository;
    }

    public String log() {

        if( repository.isEmpty() ) return "No sql stored.";

        StringBuilder sb = new StringBuilder();
        sb.append( ">> Sql Repository\n" );
        repository.values().forEach( sql -> {
            sb.append('\n').append(LINE).append('\n').append( "SQL ID :" ).append( sql.id() );
            sb.append('\n').append(LINE).append('\n');
            sb.append( sql );
        });
        sb.append('\n').append( LINE );

        return sb.toString();

    }

    public void readFrom( String resourcePath ) throws DuplicatedQueryExistException {
        if( Strings.isEmpty(resourcePath) ) return;
        for( URL url : Classes.findResources(resourcePath) ) {
            read( url );
        }
    }

    private void read( URL url ) throws DuplicatedQueryExistException {

        SqlMaker sqlMaker = new SqlMaker();

        String baseId = Files.nameWithoutExtension( url );

        Xml xml = new XmlDeformed( url );

        for( Node node : xml.getChildElements( "sql") ) {

            String id = node.getAttr( "id" );

            Assert.notEmpty( id, "SQL ID is missing in resource({})\n{}", url, node );

            if( contains( String.format("%s.%s", baseId, id) ) )
                throw new DuplicatedQueryExistException( "SQL ID({}.{}) is duplicated in resource({})", baseId, id, url );

            put( sqlMaker.make(baseId, id, node) );

        }

    }

}
