package io.nayasis.spring.extension.sql.phase.element;

import io.nayasis.basica.base.Strings;
import io.nayasis.spring.extension.sql.phase.element.abstracts.BaseSql;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;

import java.util.List;

public class ElseIfSql extends IfSql {

	public ElseIfSql( String testExpression ) throws ParseException {
		super( testExpression );
	}

	public ElseIfSql( String testExpression, List<BaseSql> children ) {
		super( testExpression, children );
	}

	public ElseIfSql( Expression testExpression, List<BaseSql> children ) {
		super( testExpression, children );
	}

	private void toString( StringBuilder buffer, BaseSql node, int depth ) {

		String tab = Strings.lpad( "", depth * 2, ' ' );

		buffer.append( String.format( "%s%s", tab, node.toString() ) );

	}

	@Override
	public String toString() throws ParseException {

		StringBuilder sb = new StringBuilder();

		sb.append( String.format("[ElseIF test='%s']\n", getExpression()) );

		for( BaseSql node : children ) {
			toString( sb, node, 1 );
		}

		return sb.toString();

	}

}
