package io.nayasis.spring.extension.sql.phase.element;

import io.nayasis.basica.base.Strings;
import io.nayasis.spring.extension.sql.phase.element.abstracts.SqlElement;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;

import java.util.List;

public class ElseIfSqlElement extends IfSqlElement {

	public ElseIfSqlElement( String testExpression ) throws ParseException {
		super( testExpression );
	}

	public ElseIfSqlElement( String testExpression, List<SqlElement> children ) {
		super( testExpression, children );
	}

	public ElseIfSqlElement( Expression testExpression, List<SqlElement> children ) {
		super( testExpression, children );
	}

	private void toString( StringBuilder buffer, SqlElement node, int depth ) {

		String tab = Strings.lpad( "", depth * 2, ' ' );

		buffer.append( String.format( "%s%s", tab, node.toString() ) );

	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append( String.format("[ElseIF test='%s']\n", getTestExpression()) );

		for( SqlElement node : children ) {
			toString( sb, node, 1 );
		}

		return sb.toString();

	}

}
