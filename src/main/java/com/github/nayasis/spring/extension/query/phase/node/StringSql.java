package com.github.nayasis.spring.extension.query.phase.node;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.spring.extension.query.entity.QueryParameter;
import com.github.nayasis.spring.extension.query.phase.node.abstracts.BaseSql;

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
    public void append( BaseSql sql ) {}

	public String toString() {
		return text;
	}

}
