package com.github.nayasis.spring.extension.query.sql.mapper.implement;

import com.github.nayasis.basica.file.Files;
import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;
import com.github.nayasis.spring.extension.query.sql.mapper.TypeMapperIF;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class BlobMapper implements TypeMapperIF<byte[]> {

	private static final String ERROR_MSG = "Blob is not implemented. {}";

	@Override
    public void setParameter( PreparedStatement ps, int index, byte[] param ) throws SQLException {
		ByteArrayInputStream bis = new ByteArrayInputStream( param );
		try {
	        ps.setBinaryStream( index, bis, param.length );
        } catch( SQLException e ) {
			log.error( ERROR_MSG, getDriverInfo(ps) );
        	throw e;
        } finally {
			Files.close( bis );
		}
    }

	@Override
	public void setOutParameter( CallableStatement cs, int index ) throws SQLException {
		cs.registerOutParameter( index, SqlType.find(byte[].class).code() );
	}

	@Override
    public byte[] getResult( ResultSet rs, String columnName ) throws SQLException {
		try {
	        return getBytes( rs.getBlob(columnName) );
        } catch( SQLException e ) {
        	log.info( ERROR_MSG, getDriverInfo(rs) );
			throw e;
        }
    }

	@Override
    public byte[] getResult( ResultSet rs, int columnIndex ) throws SQLException {
		try {
	        return getBytes( rs.getBlob(columnIndex) );
        } catch( SQLException e ) {
        	log.info( ERROR_MSG, getDriverInfo(rs) );
			throw e;
        }
    }

	@Override
    public byte[] getResult( CallableStatement cs, int columnIndex ) throws SQLException {
		try {
	        return getBytes( cs.getBlob(columnIndex) );
        } catch( SQLException e ) {
        	log.info( ERROR_MSG, getDriverInfo( cs ) );
			throw e;
        }
    }

	private byte[] getBytes( Blob blob ) throws SQLException {
		if( blob == null ) return new byte[] {};
		return blob.getBytes( 1, (int) blob.length() );
	}

	private String getDriverInfo( Connection connection ) {
		String driverInfo = "";
		if( connection != null ) {
			try {
				DatabaseMetaData dbMeta = connection.getMetaData();
	            driverInfo = String.format( "(Driver : %s(%s), URL : %s)", dbMeta.getDriverName(),  connection.getClass().getName(), dbMeta.getURL() );
			} catch( SQLException e ) {
	            log.error( e.getMessage(), e );
            }
		}
		return driverInfo;
	}

	private String getDriverInfo( Statement statement ) {
		try {
	        return statement == null ? "" : getDriverInfo( statement.getConnection() );
        } catch( SQLException e ) {
        	return "";
        }
	}

	private String getDriverInfo( ResultSet resultSet ) {
		try {
			return resultSet == null ? "" : getDriverInfo( resultSet.getStatement() );
		} catch( SQLException e ) {
			return "";
		}
	}

}
