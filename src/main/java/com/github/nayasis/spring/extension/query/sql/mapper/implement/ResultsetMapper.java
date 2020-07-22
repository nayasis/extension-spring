package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultsetMapper implements TypeMapperIF<ResultSet> {

	@Override
    public void setParameter( PreparedStatement statement, int index, ResultSet param ) throws SQLException {
		statement.setObject( index, param );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.CURSOR.code() );
	}

	@Override
    public ResultSet getResult( ResultSet rs, String columnName ) throws SQLException {
	    return (ResultSet) rs.getObject( columnName );
    }

	@Override
    public ResultSet getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return (ResultSet) rs.getObject( columnIndex );
    }

	@Override
    public ResultSet getResult( CallableStatement cs, int columnIndex ) throws SQLException {
	    return (ResultSet) cs.getObject( columnIndex );
    }

}
