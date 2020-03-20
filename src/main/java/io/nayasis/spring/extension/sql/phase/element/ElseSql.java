package io.nayasis.spring.extension.sql.phase.element;

import io.nayasis.basica.base.Strings;
import io.nayasis.spring.extension.sql.phase.element.abstracts.BaseSql;

/**
 * @author Administrator
 * @since 2015-10-23
 */
public class ElseSql extends BaseSql {

    private void toString( StringBuilder buffer, BaseSql node, int depth ) {

        String tab = Strings.lpad( "", depth * 2, ' ' );

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
