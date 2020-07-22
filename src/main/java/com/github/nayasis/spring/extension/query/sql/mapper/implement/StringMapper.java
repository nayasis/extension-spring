package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StringMapper implements TypeMapperIF<String> {

	@Override
    public void setParameter( PreparedStatement ps, int index, String param ) throws SQLException {
		ps.setString( index, param );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(String.class).code() );
	}

	@Override
    public String getResult( ResultSet rs, String columnName ) throws SQLException {
	    return rs.getString( columnName );
    }

	@Override
    public String getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return rs.getString( columnIndex );
    }

	@Override
    public String getResult( CallableStatement cs, int columnIndex ) throws SQLException {
	    return cs.getString( columnIndex );
    }

}
