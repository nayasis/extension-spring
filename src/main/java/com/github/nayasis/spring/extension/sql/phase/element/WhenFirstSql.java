package com.github.nayasis.spring.extension.sql.phase.element;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.basica.expression.Expression;
import com.github.nayasis.spring.extension.sql.phase.element.abstracts.BaseSql;

import java.util.List;

public class WhenFirstSql extends IfSql {

	public WhenFirstSql( String testExpression, List<BaseSql> children ) {
		super( testExpression, children );
	}

	public WhenFirstSql( Expression testExpression, List<BaseSql> children ) {
		super( testExpression, children );
	}

	private void toString( StringBuilder buffer, BaseSql node, int depth ) {
		String tab = Strings.line( ' ', depth * 2 );
		buffer.append( String.format( "%s%s", tab, node.toString() ) );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( String.format("[WHEN test='%s']\n",expression) );
		for( BaseSql node : children ) {
			toString( sb, node, 1 );
		}
		return sb.toString();
	}

}
