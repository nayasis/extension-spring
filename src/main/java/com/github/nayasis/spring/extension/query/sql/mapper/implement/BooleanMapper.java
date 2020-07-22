package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanMapper implements TypeMapperIF<Boolean> {

	@Override
    public void setParameter( PreparedStatement statement, int index, Boolean param ) throws SQLException {
		statement.setBoolean( index, param );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(Boolean.class).code() );
	}

	@Override
    public Boolean getResult( ResultSet rs, String columnName ) throws SQLException {
		return rs.getBoolean( columnName );
    }

	@Override
    public Boolean getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return rs.getBoolean( columnIndex );
    }

	@Override
    public Boolean getResult( CallableStatement cs, int columnIndex ) throws SQLException {
		return cs.getBoolean( columnIndex );
    }

}
