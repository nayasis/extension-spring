package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.basica.base.Types;
import com.github.nayasis.basica.file.Files;
import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobBoxedMapper implements TypeMapperIF<Byte[]> {

    @Override
    public void setParameter( PreparedStatement ps, int index, Byte[] param ) throws SQLException {
        ByteArrayInputStream is = new ByteArrayInputStream( Types.toPrimitive(param) );
		try {
			ps.setBinaryStream( index, is, param.length );
		} finally {
			Files.close(is);
		}
	}

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(Byte[].class).code() );
	}

	@Override
    public Byte[] getResult( ResultSet rs, String columnName ) throws SQLException {
		return toBytes( rs.getBlob(columnName) );
    }

	@Override
    public Byte[] getResult( ResultSet rs, int columnIndex ) throws SQLException {
		return toBytes( rs.getBlob(columnIndex) );
	}

	@Override
    public Byte[] getResult( CallableStatement cs, int columnIndex ) throws SQLException {
		return toBytes( cs.getBlob(columnIndex) );
	}

	private Byte[] toBytes( Blob blob ) throws SQLException {
	    return blob == null ? new Byte[] {} : Types.toBoxed( blob.getBytes(1, (int) blob.length()) );
    }


}
