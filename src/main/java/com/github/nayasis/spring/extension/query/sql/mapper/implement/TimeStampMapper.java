package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * TimeStampMapper
 *
 * java.sql.Date handle data as Date only and miss time information(HH:MI:SS).<br>
 * So It is necessary to handle data as TimeStamp
 */
public class TimeStampMapper implements TypeMapperIF<Date> {

	@Override
    public void setParameter( PreparedStatement ps, int index, Date param ) throws SQLException {
		ps.setTimestamp( index, toTimestamp(param) );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.TIMESTAMP.code() );
	}

	@Override
    public Date getResult( ResultSet rs, String columnName ) throws SQLException {
		Timestamp timestamp = rs.getTimestamp( columnName );
		return toDate( timestamp );
	}

	@Override
    public Date getResult( ResultSet rs, int columnIndex ) throws SQLException {
		Timestamp timestamp = rs.getTimestamp( columnIndex );
		return toDate( timestamp );
	}

	@Override
    public Date getResult( CallableStatement cs, int columnIndex ) throws SQLException {
		Timestamp timestamp = cs.getTimestamp( columnIndex );
		return toDate( timestamp );
	}

	private Timestamp toTimestamp( Date param ) {
		return param == null ? null : new Timestamp(param.getTime());
	}

	private Date toDate( Timestamp timestamp ) {
		return timestamp == null ? null : new Date(timestamp.getTime());
	}

}
