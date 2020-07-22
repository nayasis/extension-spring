package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DateMapper implements TypeMapperIF<Date> {

	@Override
    public void setParameter( PreparedStatement ps, int index, Date param ) throws SQLException {
		ps.setDate( index, new java.sql.Date(param.getTime()) );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(Date.class).code() );
	}

	@Override
    public Date getResult( ResultSet rs, String columnName ) throws SQLException {
	    return rs.getDate( columnName );
    }

	@Override
    public Date getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return rs.getDate( columnIndex );
    }

	@Override
    public Date getResult( CallableStatement cs, int columnIndex ) throws SQLException {
	    return cs.getDate( columnIndex );
    }

}
