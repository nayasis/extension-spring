package com.github.nayasis.spring.extension.sql.phase.element;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.spring.extension.sql.entity.QueryParameter;
import com.github.nayasis.spring.extension.sql.phase.element.abstracts.BaseSql;
import org.springframework.expression.ParseException;

import java.util.ArrayList;
import java.util.List;

public class CaseSql extends BaseSql {

	private boolean childrenSorted = false;

	private void sortElseElementToLast() {

		if( childrenSorted ) return;

		List<BaseSql> caseElements = new ArrayList<>();

		// gethering [if & else] element

		for( BaseSql element : children ) {
			if( element instanceof IfSql || element instanceof ElseSql ) {
				caseElements.add( element );
			}
		}

		// move [else] element to last in gathering list
		for( int i = 0, iCnt = caseElements.size() - 1; i <= iCnt; i++ ) {

			BaseSql element = caseElements.get( i );

			if( element instanceof ElseSql ) {
				caseElements.add( caseElements.remove(i) );
				i--; iCnt--;
			}

		}

		// make first if !!
		for( int i = 0, iCnt = caseElements.size() - 1; i <= iCnt; i++ ) {

			BaseSql element = caseElements.get( i );

			if( element instanceof ElseSql ) continue;

			IfSql ifSqlElement = (IfSql) element;

			if( i == 0 ) {
				if( isElseIfSeries( ifSqlElement ) ) {
					caseElements.set( i,  ifSqlElement.toWhenFirstSql() );
				}
			} else {
				if( isIfSeries( ifSqlElement ) ) {
					caseElements.set( i,  ifSqlElement.toWhenSql() );
				}
			}

		}

		// change original children
		List<BaseSql> newChildren = new ArrayList<>();

		for( BaseSql element : children ) {
			if( element instanceof IfSql || element instanceof ElseSql ) {
				newChildren.add( caseElements.remove( 0 ) );
			} else {
				newChildren.add( element );
			}
		}

		children.clear();
		children.addAll( newChildren );

     	childrenSorted = true;

	}

	private void toString( StringBuilder buffer, BaseSql node, int depth ) {
		String tab = Strings.line( ' ', depth*2 );
		buffer.append( String.format( "%s%s", tab, node.toString() ) );
	}

	public String toString() {

		sortElseElementToLast();

		StringBuilder sb = new StringBuilder();
		sb.append( "[CASE]\n" );
		for( BaseSql node : children ) {
			toString( sb, node, 0 );
		}
		return sb.toString();

	}

	@Override
	public String toString( QueryParameter param ) throws ParseException {
		sortElseElementToLast();
		return super.toString( param );
	}

	private boolean isElseIfSeries( BaseSql element ) {
		Class klass = element.getClass();
		if( klass == ElseIfSql.class ) return true;
		return klass == WhenSql.class;

	}

	private boolean isIfSeries( BaseSql element ) {
		Class klass = element.getClass();
		return klass == IfSql.class;
	}

}
