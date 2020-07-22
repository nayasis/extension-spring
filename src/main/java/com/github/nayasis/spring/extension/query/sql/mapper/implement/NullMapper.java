package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class NullMapper implements TypeMapperIF<Object> {

	@Override
    public void setParameter( PreparedStatement ps, int index, Object param ) throws SQLException {
	    ps.setNull( index, Types.NULL );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, Types.NULL );
	}

	@Override
    public Object getResult( ResultSet rs, String columnName ) throws SQLException {
		return rs.getObject( columnName );
    }

	@Override
    public Object getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return rs.getObject( columnIndex );
    }

	@Override
    public Object getResult( CallableStatement cs, int columnIndex ) throws SQLException {
		return cs.getObject( columnIndex );
    }

}
