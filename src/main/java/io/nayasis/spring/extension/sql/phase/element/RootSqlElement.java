package io.nayasis.spring.extension.sql.phase.element;

import io.nayasis.spring.extension.sql.phase.element.abstracts.SqlElement;
import io.nayasis.spring.extension.sql.phase.element.exception.QueryParseException;


public class RootSqlElement extends SqlElement {

	private String id = null;

	public RootSqlElement( String id ) throws QueryParseException {
		this.id = id;
	}

	public void setMainId( String mainId ) {
		setMainId( mainId, this );
	}

	private void setMainId( String mainId, SqlElement node ) {

		if( node instanceof RefSqlElement ) {
			((RefSqlElement) node).includeMainId( mainId );
		}

		for( SqlElement child : node.children() ) {
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

		for( SqlElement child : children ) {
			sb.append( child.toString() );
		}

		return sb.toString();

	}
}
