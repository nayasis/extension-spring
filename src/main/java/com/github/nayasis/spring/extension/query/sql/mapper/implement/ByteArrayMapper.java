package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteArrayMapper implements TypeMapperIF<byte[]> {

	@Override
    public void setParameter( PreparedStatement ps, int index, byte[] param ) throws SQLException {
		ps.setBytes( index, param );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(byte[].class).code() );
	}

	@Override
    public byte[] getResult( ResultSet rs, String columnName ) throws SQLException {
		return rs.getBytes( columnName );
    }

	@Override
    public byte[] getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return rs.getBytes( columnIndex );
    }

	@Override
    public byte[] getResult( CallableStatement cs, int columnIndex ) throws SQLException {
		return cs.getBytes( columnIndex );
    }

}
