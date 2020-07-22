package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigIntegerMapper implements TypeMapperIF<BigInteger> {

	@Override
    public void setParameter( PreparedStatement statement, int index, BigInteger param ) throws SQLException {
		statement.setBigDecimal( index, new BigDecimal(param) );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(BigInteger.class).code() );
	}

	@Override
    public BigInteger getResult( ResultSet rs, String columnName ) throws SQLException {
		return rs.getBigDecimal( columnName ).toBigInteger();
    }

	@Override
    public BigInteger getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return rs.getBigDecimal( columnIndex ).toBigInteger();
    }

	@Override
    public BigInteger getResult( CallableStatement cs, int columnIndex ) throws SQLException {
	    return cs.getBigDecimal( columnIndex ).toBigInteger();
    }

}
