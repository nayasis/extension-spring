package com.github.nayasis.spring.extension.sql.phase.node;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.spring.extension.sql.phase.node.abstracts.BaseSql;

/**
 * @author Administrator
 * @since 2015-10-23
 */
public class ElseSql extends BaseSql {

    private void toString( StringBuilder buffer, BaseSql node, int depth ) {
        String tab = Strings.line( ' ', depth*2 );
        buffer.append( String.format( "%s%s", tab, node.toString() ) );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( "[ELSE]\n" );
        for( BaseSql node : children ) {
            toString( sb, node, 1 );
        }
        return sb.toString();
    }

}
