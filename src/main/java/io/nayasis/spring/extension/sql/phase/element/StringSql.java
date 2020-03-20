package io.nayasis.spring.extension.sql.phase.element;

import io.nayasis.basica.base.Strings;
import io.nayasis.spring.extension.sql.entity.QueryParameter;
import io.nayasis.spring.extension.sql.phase.element.abstracts.BaseSql;

public class StringSql extends BaseSql {

	private String text;

	public StringSql( String text ) {
		this.text = Strings.nvl( text );
	}

	@Override
    public String toString( QueryParameter param ) {
		return text;
    }

	@Override
    public void append( BaseSql sqlElement ) {}

	public String toString() {
		return text;
	}

}
