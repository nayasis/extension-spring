package com.github.nayasis.spring.extension.sql.phase.node;

import com.github.nayasis.spring.extension.sql.entity.QueryParameter;
import com.github.nayasis.spring.extension.sql.phase.node.abstracts.BaseSql;

public class RefSql extends BaseSql {

	private String refId;

	public RefSql( String refId ) {
		this.refId = refId;
	}

	/**
	 * MainId가 세팅되지 않은 참조ID는
	 * Root Sql MainId 가 세팅될 경우, 이에 맞도록 참조ID를 변경시킨다.
	 *
	 * @param mainId main id
	 */
	public void resolveMainId( String mainId ) {
		if( refId.contains( "." ) ) {
			refId = String.format( "%s.%s", mainId, refId );
		}
	}

	@Override
    public String toString( QueryParameter param ) {

		if( refId == null ) return "";

		return "";

		//TODO : SqlRepository 구현

//		SqlNode sql = SqlRepository.get( referenceSqlId );
//
//		if( sql == null ) throw new SqlConfigurationException( "refId[{}] is not exists.", referenceSqlId );
//
//		return sql.getText( param );


	}

	public String toString() {
		return refId == null ? "" : String.format( "<ref id=\"%s\"/>", refId );
	}

}
