package com.github.nayasis.spring.extension.sql.phase.node;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.basica.expression.Expression;
import com.github.nayasis.spring.extension.sql.entity.QueryParameter;
import com.github.nayasis.spring.extension.sql.phase.node.abstracts.BaseSql;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ParseException;

import java.util.List;

public class IfSql extends BaseSql {

	protected Expression expression;

	public IfSql( String expression ) {
		this.expression = Expression.of( expression, true );
	}

	public IfSql( String expression, List<BaseSql> children ) throws ParseException {
	    this( expression );
		this.children = children;
	}

    public IfSql( Expression expression, List<BaseSql> children ) {
	    this.expression = expression;
        this.children   = children;
    }

	@Override
    public String toString( QueryParameter param ) {
		return isTrue( param ) ? super.toString( param ) : "";
	}

	private void toString( StringBuilder buffer, BaseSql node, int depth ) {
		String tab = Strings.line( ' ', depth*2 );
		buffer.append( String.format( "%s%s", tab, node.toString() ) );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( String.format("[IF test='%s']\n", expression) );
		for( BaseSql node : children ) {
			toString( sb, node, 1 );
		}
		return sb.toString();

	}

    public boolean isTrue( Object param ) throws EvaluationException {
		return expression.test( param );
	}

	protected WhenFirstSql toWhenFirstSql() {
		return new WhenFirstSql( expression, children );
	}

	protected WhenSql toWhenSql() {
		return new WhenSql( expression, children );
	}

}
