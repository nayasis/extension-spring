package io.nayasis.spring.extension.sql.phase.element;

import io.nayasis.basica.base.Strings;
import io.nayasis.spring.extension.sql.phase.element.abstracts.SqlElement;

public class StringSqlElement extends SqlElement {

	private String text;

	public StringSqlElement( String text ) {
		this.text = Strings.nvl( text );
	}

	@Override
    public String toString( Object param ) {
		return text;
    }

	@Override
    public void append( SqlElement sqlElement ) {}

	public String toString() {
		return text;
	}

}
