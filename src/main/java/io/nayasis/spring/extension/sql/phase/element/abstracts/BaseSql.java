package io.nayasis.spring.extension.sql.phase.element.abstracts;

import io.nayasis.spring.extension.sql.entity.QueryParameter;
import io.nayasis.spring.extension.sql.phase.element.ElseIfSql;
import io.nayasis.spring.extension.sql.phase.element.ElseSql;
import io.nayasis.spring.extension.sql.phase.element.IfSql;
import io.nayasis.spring.extension.sql.phase.element.WhenFirstSql;
import io.nayasis.spring.extension.sql.phase.element.WhenSql;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSql {

    protected List<BaseSql> children = new ArrayList<>();

	public String toString( QueryParameter param ) throws ParseException {

		StringBuilder sb = new StringBuilder();

		for( ElementText element : toStringList( param ) ) {
			sb.append( element.getText() );
		}

		return sb.toString();

	}

	public void append( BaseSql sqlElement ) {
		children.add( sqlElement );
	}

	public List<BaseSql> children() {
		return children;
	}

	protected List<ElementText> toStringList( QueryParameter param ) {

		List<ElementText> list = new ArrayList<>();

		Boolean previousCondition = null;

		for( BaseSql element : children ) {

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

	private boolean isIf( BaseSql element ) {
		Class klass = element.getClass();
		return klass == IfSql.class || klass == WhenFirstSql.class;
	}

	private boolean isElseIf( BaseSql element ) {
		Class klass = element.getClass();
		return klass == ElseIfSql.class || klass == WhenSql.class;
	}

	private boolean isElse( BaseSql element ) {
		Class<? extends BaseSql> klass = element.getClass();
		return klass == ElseSql.class && klass != WhenFirstSql.class;
	}

	private boolean getIfSeriesResult( BaseSql element, Object param ) {
		if( ! isIf(element) && ! isElseIf(element) ) return false;
		return ((IfSql) element).isTrue( param );
	}

	private boolean isElseSeries( BaseSql element ) {
		Class klass = element.getClass();
		if( klass == ElseIfSql.class ) return true;
		if( klass == WhenSql.class   ) return true;
		return klass == ElseSql.class;
	}

	protected Expression toExpression( String expression ) throws ParseException {
		ExpressionParser parser = new SpelExpressionParser();
		return parser.parseExpression( expression );
	}

}