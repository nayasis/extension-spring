package com.github.nayasis.spring.extension.sql.phase.element;

import com.github.nayasis.spring.extension.sql.phase.element.abstracts.BaseSql;
import org.springframework.expression.ParseException;


public class RootSql extends BaseSql {

	private String id;

	public RootSql( String id ) throws ParseException {
		this.id = id;
	}

	public void setMainId( String mainId ) {
		setMainId( mainId, this );
	}

	private void setMainId( String mainId, BaseSql node ) {

		if( node instanceof RefSql ) {
			((RefSql) node).includeMainId( mainId );
		}

		for( BaseSql child : node.children() ) {
			setMainId( mainId, child );
		}

	}

	public String getId() {
		return id;
	}

	public boolean isNotValid() {
		return children().size() == 0;
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();

		for( BaseSql child : children ) {
			sb.append( child.toString() );
		}

		return sb.toString();

	}
}
