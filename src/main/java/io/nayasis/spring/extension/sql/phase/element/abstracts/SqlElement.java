package io.nayasis.spring.extension.sql.phase.element.abstracts;

import io.nayasis.spring.extension.sql.entity.QueryParameter;
import io.nayasis.spring.extension.sql.phase.element.ElseIfSqlElement;
import io.nayasis.spring.extension.sql.phase.element.ElseSqlElement;
import io.nayasis.spring.extension.sql.phase.element.IfSqlElement;
import io.nayasis.spring.extension.sql.phase.element.WhenFirstSqlElement;
import io.nayasis.spring.extension.sql.phase.element.WhenSqlElement;
import io.nayasis.spring.extension.sql.phase.element.exception.QueryParseException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.ArrayList;
import java.util.List;

public abstract class SqlElement {

    protected List<SqlElement> children = new ArrayList<>();

	public String toString( QueryParameter param ) throws QueryParseException {

		StringBuilder sb = new StringBuilder();

		for( ElementText element : toStringList( param ) ) {
			sb.append( element.getText() );
		}

		return sb.toString();

	}

	public void append( SqlElement sqlElement ) {
		children.add( sqlElement );
	}

	public List<SqlElement> children() {
		return children;
	}

	protected List<ElementText> toStringList( QueryParameter param ) {

		List<ElementText> list = new ArrayList<>();

		Boolean previousCondition = null;

		for( SqlElement element : children ) {

			if( isElseSeries(element) ) {
				if( previousCondition != null && previousCondition != true ) {
					list.add( new ElementText( element.getClass(), element.toString( param ) ) );
				}
			} else {
				list.add( new ElementText( element.getClass(), element.toString( param ) ) );
			}

			if( isIf(element) ) {
				previousCondition = getIfSeriesResult( element, param );

			} else if( isElseIf( element ) ) {
				if( previousCondition == null || previousCondition == false ) {
					previousCondition = getIfSeriesResult( element, param );
				}

			} else if( isElse( element ) ) {
				previousCondition = null;
			}

		}

		return list;

	}

	private boolean isIf( SqlElement element ) {
		Class klass = element.getClass();
		return klass == IfSqlElement.class || klass == WhenFirstSqlElement.class;
	}

	private boolean isElseIf( SqlElement element ) {
		Class klass = element.getClass();
		return klass == ElseIfSqlElement.class || klass == WhenSqlElement.class;
	}

	private boolean isElse( SqlElement element ) {
		Class<? extends SqlElement> klass = element.getClass();
		return klass == ElseSqlElement.class && klass != WhenFirstSqlElement.class;
	}

	private boolean getIfSeriesResult( SqlElement element, Object param ) {
		if( ! isIf(element) && ! isElseIf(element) ) return false;
		return ((IfSqlElement) element).isTrue( param );
	}

	private boolean isElseSeries( SqlElement element ) {
		Class klass = element.getClass();
		if( klass == ElseIfSqlElement.class ) return true;
		if( klass == WhenSqlElement.class   ) return true;
		return klass == ElseSqlElement.class;
	}

	protected Expression toExpression( String expression ) throws ParseException {
		ExpressionParser parser = new SpelExpressionParser();
		return parser.parseExpression( expression );
	}

}