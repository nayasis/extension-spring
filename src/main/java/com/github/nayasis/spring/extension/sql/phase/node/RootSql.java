package com.github.nayasis.spring.extension.sql.phase.node;

import com.github.nayasis.spring.extension.sql.phase.node.abstracts.BaseSql;
import lombok.Getter;
import org.springframework.expression.ParseException;


public class RootSql extends BaseSql {

	@Getter
	private String id;

	public RootSql( String id ) throws ParseException {
		this.id = id;
	}

	public void resolveMainId( String mainId ) {
		resolveMainId( mainId, this );
	}

	private void resolveMainId( String mainId, BaseSql node ) {

		if( node instanceof RefSql )
			((RefSql)node).resolveMainId( mainId );

		node.children().forEach( child -> resolveMainId(mainId, child) );

	}

	public boolean isNotValid() {
		return children().size() == 0;
	}

}