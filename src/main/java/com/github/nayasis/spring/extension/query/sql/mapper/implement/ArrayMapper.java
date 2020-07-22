package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.basica.base.Types;
import com.github.nayasis.basica.validation.Validator;
import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings( "rawtypes" )
public class ArrayMapper implements TypeMapperIF<List> {

    @Override
    public void setParameter( PreparedStatement ps, int index, List param ) throws SQLException {
		// It works when corresponding collection type exists in database.
		ps.setArray( index, toArray(param, ps) );
    }

	@Override
    public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.ARRAY.code() );
    }

	@Override
    public List getResult( ResultSet rs, String columnName ) throws SQLException {
		return Types.toList( rs.getArray(columnName) );
	}

    @Override
    public List getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return Types.toList( rs.getArray(columnIndex) );
	}

	@Override
    public List getResult( CallableStatement cs, int columnIndex ) throws SQLException {
		return Types.toList( cs.getArray(columnIndex) );
	}

	private Array toArray( List list, PreparedStatement ps ) throws SQLException {
    	if( Validator.isEmpty(list) ) return null;
		String type = SqlType.find( list.get(0).getClass() ).name();
		return ps.getConnection().createArrayOf( type, list.toArray() );
	}

}
