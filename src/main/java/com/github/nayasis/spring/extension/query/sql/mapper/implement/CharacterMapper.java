package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacterMapper implements TypeMapperIF<Character> {

	@Override
    public void setParameter( PreparedStatement ps, int index, Character param ) throws SQLException {
		ps.setString( index, Strings.nvl( param ) );
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(Character.class).code() );
	}

	@Override
    public Character getResult( ResultSet rs, String columnName ) throws SQLException {
	    return toCharacter( rs.getString(columnName) );
    }

	@Override
    public Character getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return toCharacter( rs.getString(columnIndex) );
    }

	@Override
    public Character getResult( CallableStatement cs, int columnIndex ) throws SQLException {
		return toCharacter( cs.getString(columnIndex) );
    }

	private Character toCharacter( String value ) {
		return value == null ? null : value.charAt(0);
	}

}
