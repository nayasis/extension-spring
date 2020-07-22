package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LongMapper implements TypeMapperIF<Long> {

	@Override
    public void setParameter( PreparedStatement ps, int index, Long param ) throws SQLException {
		ps.setLong( index, param );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(Long.class).code() );
	}

	@Override
    public Long getResult( ResultSet rs, String columnName ) throws SQLException {
	    return rs.getLong( columnName );
    }

	@Override
    public Long getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return rs.getLong( columnIndex );
    }

	@Override
    public Long getResult( CallableStatement cs, int columnIndex ) throws SQLException {
	    return cs.getLong( columnIndex );
    }

}
