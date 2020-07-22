package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortMapper implements TypeMapperIF<Short> {

	@Override
    public void setParameter( PreparedStatement ps, int index, Short param ) throws SQLException {
		ps.setShort( index, param );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(Short.class).code() );
	}

	@Override
    public Short getResult( ResultSet rs, String columnName ) throws SQLException {
	    return rs.getShort( columnName );
    }

	@Override
    public Short getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return rs.getShort( columnIndex );
    }

	@Override
    public Short getResult( CallableStatement cs, int columnIndex ) throws SQLException {
	    return cs.getShort( columnIndex );
    }

}
