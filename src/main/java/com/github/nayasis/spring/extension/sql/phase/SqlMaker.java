package com.github.nayasis.spring.extension.sql.phase;

import com.github.nayasis.basica.xml.node.Node;
import com.github.nayasis.spring.extension.sql.exception.QueryParseException;
import com.github.nayasis.spring.extension.sql.phase.node.CaseSql;
import com.github.nayasis.spring.extension.sql.phase.node.ElseIfSql;
import com.github.nayasis.spring.extension.sql.phase.node.ElseSql;
import com.github.nayasis.spring.extension.sql.phase.node.ForEachSql;
import com.github.nayasis.spring.extension.sql.phase.node.GroupSql;
import com.github.nayasis.spring.extension.sql.phase.node.IfSql;
import com.github.nayasis.spring.extension.sql.phase.node.RefSql;
import com.github.nayasis.spring.extension.sql.phase.node.RootSql;
import com.github.nayasis.spring.extension.sql.phase.node.StringSql;
import com.github.nayasis.spring.extension.sql.phase.node.WhenSql;
import com.github.nayasis.spring.extension.sql.phase.node.abstracts.BaseSql;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.mvel2.CompileException;

@Slf4j
public class SqlMaker {

    public RootSql make( String id, Node xml ) {
        RootSql sql = new RootSql( id );
        make( id, sql, xml );
        return sql;
    }

    private void make( String rootId, BaseSql parent, Node xml ) {
        for( Node child : xml.getChildNodes() ) {
            BaseSql sql = toSqlNode( rootId, child );
            if( sql != null ) {
                parent.append( sql );
                make( rootId, sql, child );
            }
        }
    }

    private BaseSql toSqlNode( String rootId, Node node ) {

        if( node.isText() )
            return new StringSql(node.getText());

        if( node.isElement() ) {
            try {
                switch( node.getName() ) {
                    case "if" :
                        return new IfSql( node.getAttrIgnoreCase("test") );
                    case "elseif" :
                        return new ElseIfSql( node.getAttrIgnoreCase("test") );
                    case "else" :
                        return new ElseSql();
                    case "case" :
                        return new CaseSql();
                    case "when" :
                        return new WhenSql( node.getAttrIgnoreCase("test") );
                    case "ref" :
                        return new RefSql( node.getAttrIgnoreCase("id") );
                    case "foreach" :
                        return new ForEachSql(
                            node.getAttrIgnoreCase("key"),
                            node.getAttrIgnoreCase("open"),
                            node.getAttrIgnoreCase("close"),
                            node.getAttrIgnoreCase("concat"),
                            node.getAttrIgnoreCase("index")
                        );
                    case "group" :
                        return new GroupSql(
                            node.getAttrIgnoreCase("open"),
                            node.getAttrIgnoreCase("close"),
                            node.getAttrIgnoreCase("concat")
                        );
                }
            } catch ( CompileException e ) {
                e.getMessage(); // calculate column
                throw new QueryParseException( "Invalid expression in query(id:{})\n\nnear ... {} ...\n{}^",
                    rootId, new String(e.getExpr()), Strings.repeat( " ", e.getColumn() + 15 )
                );
            }
        }

        return null;

    }

}
