package com.github.nayasis.spring.extension.query.sql.mapper;

import com.github.nayasis.spring.extension.query.sql.mapper.implement.ArrayMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.BigDecimalMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.BlobBoxedMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.BlobMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.BooleanMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.ByteArrayMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.ByteMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.ClobMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.DoubleMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.IntegerMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.LongMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.NullMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.ObjectMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.StringMapper;
import com.github.nayasis.spring.extension.query.sql.mapper.implement.TimeStampMapper;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.ARRAY;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.BIGINT;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.BIT;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.BLOB;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.BLOB_BOXED;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.BOOLEAN;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.CHAR;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.CLOB;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.DATE;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.DECIMAL;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.DOUBLE;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.FLOAT;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.INTEGER;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.LONGVARBINARY;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.LONGVARCHAR;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.NCHAR;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.NCLOB;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.NULL;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.NUMERIC;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.NVARCHAR;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.REAL;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.REF;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.SMALLINT;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.STRUCT;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.TIME;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.TIMESTAMP;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.TINYINT;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.VARCHAR;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.find;

public class TypeMapper {

    private static Map<DataSource,Map<SqlType,TypeMapperIF>> mappers = new HashMap<>();

    static {

        put( NULL,          new NullMapper()       );
        put( BOOLEAN,       new BooleanMapper()    );
    	put( BIT,           new BooleanMapper()    );
    	put( TINYINT,       new ByteMapper()       );
    	put( SMALLINT,      new IntegerMapper()    );
    	put( INTEGER,       new IntegerMapper()    );
    	put( BIGINT,        new LongMapper()       );
    	put( FLOAT,         new LongMapper()       );
    	put( DOUBLE,        new DoubleMapper()     );
    	put( REAL,          new BigDecimalMapper() );
    	put( DECIMAL,       new BigDecimalMapper() );
    	put( NUMERIC,       new BigDecimalMapper() );
    	put( CHAR,          new StringMapper()     );
    	put( CLOB,          new ClobMapper()       );
    	put( VARCHAR,       new StringMapper()     );
    	put( LONGVARCHAR,   new StringMapper()     );
    	put( NVARCHAR,      new StringMapper()     );
    	put( NCHAR,         new StringMapper()     );
    	put( NCLOB,         new ClobMapper()       );
    	put( ARRAY,         new ArrayMapper()      );
    	put( BLOB,          new BlobMapper()       );
    	put( BLOB_BOXED,    new BlobBoxedMapper()  ); // for java
    	put( LONGVARBINARY, new BlobMapper()       );

		/**
		 * java.sql.Date handle data as Date only and miss time information(HH:MI:SS)
		 * So It is necessary to handle data as TimeStamp
		 */
		put( DATE,          new TimeStampMapper()  );
    	put( TIME,          new TimeStampMapper()  );
    	put( TIMESTAMP,     new TimeStampMapper()  );

    	put( REF,           new ObjectMapper()     );
    	put( STRUCT,        new ObjectMapper()     );

    }

    private static void put( SqlType sqlType, TypeMapperIF<?> mapper ) {
    	put( null, sqlType, mapper );
    }

	public static void put( DataSource env, SqlType sqlType, TypeMapperIF<?> mapper ) {
		if( ! mappers.containsKey(env) ) {
			mappers.put( env, new HashMap<>() );
		}
		mappers.get(env).put( sqlType, mapper );
	}

	@SuppressWarnings( "rawtypes" )
    public static TypeMapperIF get( DataSource env, SqlType sqlType ) {
		if( ! mappers.containsKey(env) ) env = null;
		TypeMapperIF mapper = mappers.get(env).get( sqlType );
		return ( mapper == null ) ? get( sqlType.code() ) : mapper;
	}

	@SuppressWarnings( "rawtypes" )
	public static TypeMapperIF get( int sqlType ) {
		return mappers.get( null ).get( find( sqlType ) );
	}

	/**
	 * autofix mapper type error
	 *
	 * @param env		db configuration environment
	 * @param sqlType 	column type
	 * @param e       	previous throwable
	 * @throws SQLException occurs when raise error in SQL handling.
     */
	public static void autofix( DataSource env, SqlType sqlType, Throwable e ) throws SQLException {
		TypeMapperIF prevMapper = get( env, sqlType );
		if( sqlType == BLOB && ! (prevMapper instanceof ByteArrayMapper) ) {
			put( env, BLOB, new ByteArrayMapper() );
			return;
		} else if( sqlType == CLOB && ! (prevMapper instanceof StringMapper) ) {
			put( env, CLOB, new StringMapper() );
			return;
		}
		throw new SQLException( String.format("there is no mapper to handle sqlType(%s) in environment(%s)", sqlType, env), e );
	}

}
