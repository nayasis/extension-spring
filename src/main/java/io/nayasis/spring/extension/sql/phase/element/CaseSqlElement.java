package io.nayasis.spring.extension.sql.phase.element;

import io.nayasis.basica.base.Strings;
import io.nayasis.spring.extension.sql.entity.QueryParameter;
import io.nayasis.spring.extension.sql.phase.element.abstracts.SqlElement;
import io.nayasis.spring.extension.sql.phase.element.exception.QueryParseException;

import java.util.ArrayList;
import java.util.List;

public class CaseSqlElement extends SqlElement {

	private boolean childrenSorted = false;

	private void sortElseElementToLast() {

		if( childrenSorted ) return;

		List<SqlElement> caseElements = new ArrayList<>();

		// gethering [if & else] element

		for( SqlElement element : children ) {
			if( element instanceof IfSqlElement || element instanceof ElseSqlElement ) {
				caseElements.add( element );
			}
		}

		// move [else] element to last in gatering list
		for( int i = 0, iCnt = caseElements.size() - 1; i <= iCnt; i++ ) {

			SqlElement element = caseElements.get( i );

			if( element instanceof ElseSqlElement ) {
				caseElements.add( caseElements.remove(i) );
				i--; iCnt--;
			}

		}

		// make first if !!
		for( int i = 0, iCnt = caseElements.size() - 1; i <= iCnt; i++ ) {

			SqlElement element = caseElements.get( i );

			if( element instanceof ElseSqlElement ) continue;

			IfSqlElement ifSqlElement = (IfSqlElement) element;

			if( i == 0 ) {
				if( isElseIfSeries( ifSqlElement ) ) {
					caseElements.set( i,  ifSqlElement.toWhenFirstSqlElement() );
				}
			} else {
				if( isIfSeries( ifSqlElement ) ) {
					caseElements.set( i,  ifSqlElement.toWhenSqlElement() );
				}
			}

		}

		// change original children
		List<SqlElement> newChildren = new ArrayList<>();

		for( SqlElement element : children ) {
			if( element instanceof IfSqlElement || element instanceof ElseSqlElement ) {
				newChildren.add( caseElements.remove( 0 ) );
			} else {
				newChildren.add( element );
			}
		}

		children.clear();
		children.addAll( newChildren );

     	childrenSorted = true;

	}

	private void toString( StringBuilder buffer, SqlElement node, int depth ) {

		String tab = Strings.lpad( "", depth * 2, ' ' );

		buffer.append( String.format( "%s%s", tab, node.toString() ) );

	}

	public String toString() {

		sortElseElementToLast();

		StringBuilder sb = new StringBuilder();

		sb.append( "[CASE]\n" );

		for( SqlElement node : children ) {
			toString( sb, node, 0 );
		}

		return sb.toString();

	}

	@Override
	public String toString( QueryParameter param ) throws QueryParseException {
		sortElseElementToLast();
		return super.toString( param );
	}

	private boolean isElseIfSeries( SqlElement element ) {
		Class klass = element.getClass();
		if( klass == ElseIfSqlElement.class ) return true;
		return klass == WhenSqlElement.class;

	}

	private boolean isIfSeries( SqlElement element ) {
		Class klass = element.getClass();
		return klass == IfSqlElement.class;
	}

}
