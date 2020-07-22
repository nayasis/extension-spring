package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

public class TimeMapper implements TypeMapperIF<Date> {

	@Override
    public void setParameter( PreparedStatement statement, int index, Date param ) throws SQLException {
		statement.setTime( index, new Time(param.getTime()) );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.TIME.code() );
	}

	@Override
    public Date getResult( ResultSet rs, String columnName ) throws SQLException {
		Time time = rs.getTime( columnName );
		return time == null ? null : new Date( time.getTime() );
    }

	@Override
    public Date getResult( ResultSet rs, int columnIndex ) throws SQLException {
		Time time = rs.getTime( columnIndex );
		return time == null ? null : new Date( time.getTime() );
    }

	@Override
    public Date getResult( CallableStatement cs, int columnIndex ) throws SQLException {
		Time time = cs.getTime( columnIndex );
		return time == null ? null : new Date( time.getTime() );
    }

}
