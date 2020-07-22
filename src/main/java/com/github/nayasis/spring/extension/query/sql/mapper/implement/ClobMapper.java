package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.basica.file.Files;
import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClobMapper implements TypeMapperIF<String> {

	@Override
    public void setParameter( PreparedStatement statement, int index, String param ) throws SQLException {
		StringReader reader = new StringReader( param );
		try {
			statement.setCharacterStream( index, reader, param.length() );
		} finally {
			Files.close( reader );
		}
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(String.class).code() );
	}

	@Override
    public String getResult( ResultSet rs, String columnName ) throws SQLException {
	    return toString( rs.getClob(columnName) );
    }

	@Override
    public String getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return toString( rs.getClob(columnIndex) );
    }

	@Override
    public String getResult( CallableStatement cs, int columnIndex ) throws SQLException {
		return toString( cs.getClob(columnIndex) );
    }

	private String toString( Clob clob ) throws SQLException {
		if( clob == null ) return null;
		return clob.getSubString( 1, (int) clob.length() );

	}

}
