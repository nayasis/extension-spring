package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerMapper implements TypeMapperIF<Integer> {

	@Override
    public void setParameter( PreparedStatement statement, int index, Integer param ) throws SQLException {
		statement.setInt( index, param );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(Integer.class).code() );
	}

	@Override
    public Integer getResult( ResultSet rs, String columnName ) throws SQLException {
	    return rs.getInt( columnName );
    }

	@Override
    public Integer getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return rs.getInt( columnIndex );
    }

	@Override
    public Integer getResult( CallableStatement cs, int columnIndex ) throws SQLException {
	    return cs.getInt( columnIndex );
    }

}
