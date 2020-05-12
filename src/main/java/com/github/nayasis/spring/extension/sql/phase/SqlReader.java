package com.github.nayasis.spring.extension.sql.phase;

import com.github.nayasis.basica.xml.node.Node;
import com.github.nayasis.spring.extension.sql.exception.QueryParseException;
import com.github.nayasis.spring.extension.sql.phase.element.CaseSql;
import com.github.nayasis.spring.extension.sql.phase.element.ElseIfSql;
import com.github.nayasis.spring.extension.sql.phase.element.ElseSql;
import com.github.nayasis.spring.extension.sql.phase.element.ForEachSql;
import com.github.nayasis.spring.extension.sql.phase.element.GroupSql;
import com.github.nayasis.spring.extension.sql.phase.element.IfSql;
import com.github.nayasis.spring.extension.sql.phase.element.RefSql;
import com.github.nayasis.spring.extension.sql.phase.element.RootSql;
import com.github.nayasis.spring.extension.sql.phase.element.StringSql;
import com.github.nayasis.spring.extension.sql.phase.element.WhenSql;
import com.github.nayasis.spring.extension.sql.phase.element.abstracts.BaseSql;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.mvel2.CompileException;

@Slf4j
public class SqlReader {

    public RootSql makeSql( String id, Node xml ) {

        RootSql sql = new RootSql( id );

        makeNode( sql, xml );

        return sql;

    }

    private void makeNode( BaseSql parent, Node xml ) {

        for( Node child : xml.getChildNodes() ) {

            if( child.isText() ) {

                parent.append( new StringSql(child.getText()) );

            } else if( child.isElement() ) {

                BaseSql appendNode = null;

                try {
                    switch( child.getName() ) {

                        case "if" :
                            appendNode = new IfSql( child.getAttrIgnoreCase("test") );
                            break;

                        case "elseif" :
                            appendNode = new ElseIfSql( child.getAttrIgnoreCase("test") );
                            break;

                        case "else" :
                            appendNode = new ElseSql();
                            break;

                        case "case" :
                            appendNode = new CaseSql();
                            break;

                        case "when" :
                            appendNode = new WhenSql( child.getAttrIgnoreCase("test") );
                            break;

                        case "ref" :
                            appendNode = new RefSql( child.getAttrIgnoreCase("id") );
                            break;

                        case "foreach" :
                            appendNode = new ForEachSql(
                                child.getAttrIgnoreCase("key"),
                                child.getAttrIgnoreCase("open"),
                                child.getAttrIgnoreCase("close"),
                                child.getAttrIgnoreCase("concat"),
                                child.getAttrIgnoreCase("index")
                            );
                            break;

                        case "group" :
                            appendNode = new GroupSql(
                                child.getAttrIgnoreCase("open"),
                                child.getAttrIgnoreCase("close"),
                                child.getAttrIgnoreCase("concat")
                            );
                            break;
                    }
                } catch ( CompileException e ) {
                    e.getMessage(); // calculate column
                    throw new QueryParseException( "Invalid expression in query(id:{})\n\nnear ... {} ...\n{}^",
                       parent.getRoot().getId(), new String(e.getExpr()), Strings.repeat( " ", e.getColumn() + 15 )
                    );
                }

                if( appendNode != null ) {
                    parent.append( appendNode );
                    makeNode( appendNode, child );
                }

            }

        }

    }


}
