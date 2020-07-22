package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigDecimalMapper implements TypeMapperIF<BigDecimal> {

	@Override
    public void setParameter( PreparedStatement statement, int index, BigDecimal param ) throws SQLException {
		statement.setBigDecimal( index, param );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(BigDecimal.class).code() );
	}

	@Override
    public BigDecimal getResult( ResultSet rs, String columnName ) throws SQLException {
	    return rs.getBigDecimal( columnName );
    }

	@Override
    public BigDecimal getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return rs.getBigDecimal( columnIndex );
    }

	@Override
    public BigDecimal getResult( CallableStatement cs, int columnIndex ) throws SQLException {
	    return cs.getBigDecimal( columnIndex );
    }

}
