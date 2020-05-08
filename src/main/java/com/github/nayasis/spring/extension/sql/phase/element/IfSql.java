package com.github.nayasis.spring.extension.sql.phase.element;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.spring.extension.sql.entity.QueryParameter;
import com.github.nayasis.spring.extension.sql.phase.element.abstracts.BaseSql;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.List;

public class IfSql extends BaseSql {

	private Expression expression;

	public IfSql( String expression ) throws ParseException {
		this.expression = toExpression( expression );
	}

	public IfSql( String expression, List<BaseSql> children ) throws ParseException {
	    this( expression );
		this.children = children;
	}

    public IfSql( Expression expression, List<BaseSql> children ) {
	    this.expression = expression;
        this.children = children;
    }

	@Override
    public String toString( QueryParameter param ) {
		return isTrue( param ) ? super.toString( param ) : "";
	}

	private void toString( StringBuilder buffer, BaseSql node, int depth ) {

		String tab = Strings.lpad( "", depth * 2, ' ' );

		buffer.append( String.format( "%s%s", tab, node.toString() ) );

	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append( String.format("[IF test='%s']\n", expression ) );

		for( BaseSql node : children ) {
			toString( sb, node, 1 );
		}

		return sb.toString();

	}

    public boolean isTrue( Object param ) throws EvaluationException {
        EvaluationContext context = new StandardEvaluationContext(param);
        return expression.getValue( context, Boolean.class );
	}

	protected String getExpression() {
		return expression.getExpressionString();
	}

	protected WhenFirstSql toWhenFirstSqlElement() {
		return new WhenFirstSql( expression, children );
	}

	protected WhenSql toWhenSqlElement() {
		return new WhenSql( expression, children );
	}

}
