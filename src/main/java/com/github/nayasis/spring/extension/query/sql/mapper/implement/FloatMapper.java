package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatMapper implements TypeMapperIF<Float> {

	@Override
    public void setParameter( PreparedStatement ps, int index, Float param ) throws SQLException {
		ps.setFloat( index, param );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find( Float.class ).code() );
	}

	@Override
    public Float getResult( ResultSet rs, String columnName ) throws SQLException {
	    return rs.getFloat( columnName );
    }

	@Override
    public Float getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return rs.getFloat( columnIndex );
    }

	@Override
    public Float getResult( CallableStatement cs, int columnIndex ) throws SQLException {
	    return cs.getFloat( columnIndex );
    }

}
