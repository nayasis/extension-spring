package com.github.nayasis.spring.extension.sql.phase.element;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.spring.extension.sql.entity.QueryParameter;
import com.github.nayasis.spring.extension.sql.phase.element.abstracts.BaseSql;
import com.github.nayasis.spring.extension.sql.phase.element.abstracts.ElementText;
import org.springframework.expression.ParseException;

import java.util.ArrayList;
import java.util.List;

public class GroupSql extends BaseSql {

	private String open;
	private String close;
	private String concater;

	public GroupSql( String open, String close, String concater ) {

		this.open     = Strings.trim( open );
		this.close    = Strings.trim( close );
		this.concater = Strings.trim( concater );

	}

	@Override
    public String toString( QueryParameter param ) throws ParseException {

		List<StringBuilder> paragraph = getParagraph( param );

		StringBuilder sb = new StringBuilder();

		sb.append( open );
		sb.append( Strings.join( paragraph, concater ) );
		sb.append( close );

		return sb.toString();

	}

	private List<StringBuilder> getParagraph( QueryParameter param ) {

		List<StringBuilder> paragraph = new ArrayList<>();

		StringBuilder buffer = new StringBuilder();

		for( ElementText element : toStringList( param ) ) {

			buffer.append( element.getText() );

			if( concater.isEmpty() ) continue;
			if( element.getKlass() == StringSql.class ) continue;
			if( Strings.isBlank(element.getText()) ) continue;

			paragraph.add( buffer );

			buffer = new StringBuilder();

		}

		if( ! Strings.isBlank(buffer) ) {
			paragraph.add( buffer );
		}
		return paragraph;
	}

	private void toString( StringBuilder buffer, BaseSql node, int depth ) {

		String tab = Strings.lpad( "", depth * 2, ' ' );

		buffer.append( String.format( "%s%s", tab, node.toString() ) );

	}

	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append( String.format("[GROUP open='%s' close='%s' concater='%s']\n", open, close, concater ) );

		for( BaseSql node : children ) {
			toString( sb, node, 0 );
		}

		return sb.toString();

	}

}
