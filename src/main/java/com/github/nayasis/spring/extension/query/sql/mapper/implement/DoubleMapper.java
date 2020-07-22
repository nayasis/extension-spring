package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.find;

public class DoubleMapper implements TypeMapperIF<Double> {

	@Override
    public void setParameter( PreparedStatement ps, int index, Double param ) throws SQLException {
		ps.setDouble( index, param );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, find(Double.class).code() );
	}

	@Override
    public Double getResult( ResultSet rs, String columnName ) throws SQLException {
	    return rs.getDouble( columnName );
    }

	@Override
    public Double getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return rs.getDouble( columnIndex );
    }

	@Override
    public Double getResult( CallableStatement cs, int columnIndex ) throws SQLException {
	    return cs.getDouble( columnIndex );
    }

}
