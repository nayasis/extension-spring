package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.basica.base.Types;
import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteBoxedArrayMapper implements TypeMapperIF<Byte[]> {

	@Override
    public void setParameter( PreparedStatement ps, int index, Byte[] param ) throws SQLException {
		ps.setBytes( index, Types.toPrimitive(param) );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(Byte[].class).code() );
	}

	@Override
    public Byte[] getResult( ResultSet rs, String columnName ) throws SQLException {
		return Types.toBoxed( rs.getBytes(columnName) );
    }

	@Override
    public Byte[] getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return Types.toBoxed( rs.getBytes( columnIndex ) );
    }

	@Override
    public Byte[] getResult( CallableStatement cs, int columnIndex ) throws SQLException {
		return Types.toBoxed( cs.getBytes(columnIndex) );
    }

}