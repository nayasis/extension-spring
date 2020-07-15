package com.github.nayasis.spring.extension.sql.phase.element;

import com.github.nayasis.basica.expression.Expression;
import com.github.nayasis.spring.extension.sql.phase.element.abstracts.BaseSql;

import java.util.List;

public class WhenSql extends ElseIfSql {

	public WhenSql( String testExpression ) {
		super( testExpression );
	}

	public WhenSql( String testExpression, List<BaseSql> children ) {
		super( testExpression, children );
	}

	public WhenSql( Expression testExpression, List<BaseSql> children ) {
		super( testExpression, children );
	}

	private void toString( StringBuilder buffer, BaseSql node, int depth ) {
		buffer.append( String.format( "%s%s", getTab(depth), node.toString() ) );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( String.format("[WHEN test='%s']\n", expression) );
		for( BaseSql node : children ) {
			toString( sb, node, 1 );
		}
		return sb.toString();
	}

}
