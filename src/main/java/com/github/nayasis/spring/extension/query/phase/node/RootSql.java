package com.github.nayasis.spring.extension.query.phase.node;

import com.github.nayasis.spring.extension.query.phase.node.abstracts.BaseSql;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.expression.ParseException;


public class RootSql extends BaseSql {

	@Getter @Accessors(fluent=true)
	private String id;

	public RootSql( String id ) throws ParseException {
		this.id = id;
	}

	public RootSql resolveMainId( String mainId ) {
		resolveMainId( mainId, this );
		return this;
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