package com.github.nayasis.spring.extension.sql.phase;

import com.github.nayasis.basica.xml.node.Node;
import com.github.nayasis.spring.extension.sql.phase.element.RootSql;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlNode {

    @Getter @Setter @Accessors(fluent=true)
    private String  id;

    private RootSql sql;

    public SqlNode( String id, Node xml ) {

        this.id = id;

        sql = new RootSql( id );





    }



}
