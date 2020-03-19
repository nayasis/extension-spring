package io.nayasis.spring.extension.sql.phase.element;

import io.nayasis.basica.base.Strings;
import io.nayasis.spring.extension.sql.phase.element.abstracts.SqlElement;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.List;

public class IfSqlElement extends SqlElement {

	private Expression testExpression;

	public IfSqlElement( String testExpression ) throws ParseException {
		this.testExpression = toExpression( testExpression );
	}

	public IfSqlElement( String testExpression, List<SqlElement> children ) {
	    this( testExpression );
		this.children = children;
	}

    public IfSqlElement( Expression testExpression, List<SqlElement> children ) {
	    this.testExpression = testExpression;
        this.children = children;
    }

	@Override
    public String toString( Object param ) {
		return isTrue( param ) ? super.toString( param ) : "";
	}

	private void toString( StringBuilder buffer, SqlElement node, int depth ) {

		String tab = Strings.lpad( "", depth * 2, ' ' );

		buffer.append( String.format( "%s%s", tab, node.toString() ) );

	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append( String.format("[IF test='%s']\n", testExpression) );

		for( SqlElement node : children ) {
			toString( sb, node, 1 );
		}

		return sb.toString();

	}

    public boolean isTrue( Object param ) throws EvaluationException {
        EvaluationContext context = new StandardEvaluationContext(param);
        return testExpression.getValue( context, Boolean.class );
	}

	protected String getTestExpression() {
		return testExpression.getExpressionString();
	}


	protected WhenFirstSqlElement toWhenFirstSqlElement() {
		return new WhenFirstSqlElement( testExpression, children );
	}

	protected WhenSqlElement toWhenSqlElement() {
		return new WhenSqlElement( testExpression, children );
	}

}
