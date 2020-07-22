package com.github.nayasis.spring.extension.query.sql.mapper;

import com.github.nayasis.basica.base.Types;
import com.github.nayasis.basica.model.NDate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public enum SqlType {

	BIT                     ( java.sql.Types.BIT                     , "BIT"                     ),
	TINYINT                 ( java.sql.Types.TINYINT                 , "TINYINT"                 ),
	SMALLINT                ( java.sql.Types.SMALLINT                , "SMALLINT"                ),
	INTEGER                 ( java.sql.Types.INTEGER                 , "INTEGER"                 ),
	BIGINT                  ( java.sql.Types.BIGINT                  , "BIGINT"                  ),
	FLOAT                   ( java.sql.Types.FLOAT                   , "FLOAT"                   ),
	REAL                    ( java.sql.Types.REAL                    , "REAL"                    ),
	DOUBLE                  ( java.sql.Types.DOUBLE                  , "DOUBLE"                  ),
	NUMERIC                 ( java.sql.Types.NUMERIC                 , "NUMERIC"                 ),
	DECIMAL                 ( java.sql.Types.DECIMAL                 , "DECIMAL"                 ),
	CHAR                    ( java.sql.Types.CHAR                    , "CHAR"                    ),
	VARCHAR                 ( java.sql.Types.VARCHAR                 , "VARCHAR"                 ),
	LONGVARCHAR             ( java.sql.Types.LONGVARCHAR             , "LONGVARCHAR"             ),
	DATE                    ( java.sql.Types.DATE                    , "DATE"                    ),
	TIME                    ( java.sql.Types.TIME                    , "TIME"                    ),
	TIMESTAMP               ( java.sql.Types.TIMESTAMP               , "TIMESTAMP"               ),
	BINARY                  ( java.sql.Types.BINARY                  , "BINARY"                  ),
	VARBINARY               ( java.sql.Types.VARBINARY               , "VARBINARY"               ),
	LONGVARBINARY           ( java.sql.Types.LONGVARBINARY           , "LONGVARBINARY"           ),
	NULL                    ( java.sql.Types.NULL                    , "NULL"                    ),
	OTHER                   ( java.sql.Types.OTHER                   , "OTHER"                   ),
	JAVA_OBJECT             ( java.sql.Types.JAVA_OBJECT             , "JAVA_OBJECT"             ),
	DISTINCT                ( java.sql.Types.DISTINCT                , "DISTINCT"                ),
	STRUCT                  ( java.sql.Types.STRUCT                  , "STRUCT"                  ),
	ARRAY                   ( java.sql.Types.ARRAY                   , "ARRAY"                   ),
	BLOB                    ( java.sql.Types.BLOB                    , "BLOB"                    ),
	CLOB                    ( java.sql.Types.CLOB                    , "CLOB"                    ),
	REF                     ( java.sql.Types.REF                     , "REF"                     ),
	DATALINK                ( java.sql.Types.DATALINK                , "DATALINK"                ),
	BOOLEAN                 ( java.sql.Types.BOOLEAN                 , "BOOLEAN"                 ),
	ROWID                   ( java.sql.Types.ROWID                   , "ROWID"                   ),
	NCHAR                   ( java.sql.Types.NCHAR                   , "NCHAR"                   ),
	NVARCHAR                ( java.sql.Types.NVARCHAR                , "NVARCHAR"                ),
	LONGNVARCHAR            ( java.sql.Types.LONGNVARCHAR            , "LONGNVARCHAR"            ),
	NCLOB                   ( java.sql.Types.NCLOB                   , "NCLOB"                   ),
	SQLXML                  ( java.sql.Types.SQLXML                  , "SQLXML"                  ),
	REF_CURSOR              ( java.sql.Types.REF_CURSOR              , "REF_CURSOR"              ),
	TIME_WITH_TIMEZONE      ( java.sql.Types.TIME_WITH_TIMEZONE      , "TIME_WITH_TIMEZONE"      ),
	TIMESTAMP_WITH_TIMEZONE ( java.sql.Types.TIMESTAMP_WITH_TIMEZONE , "TIMESTAMP_WITH_TIMEZONE" ),

	CURSOR                  ( -10                             , "CURSOR"                  ), // for oracle cursor
	LIST                    ( 9001                            , "LIST"                    ), // for Typecasting
	BLOB_BOXED              ( 9002                            , "BLOB_BOXED"              ), // for Byte[]
	;

	private int     code;
	private String  name;

	private static Map<Integer,SqlType> codes = new HashMap<>();
	private static Map<String,SqlType>  names = new HashMap<>();

	static {
		for( SqlType type : SqlType.values() ) {
			codes.put( type.code, type );
			names.put( type.name, type );
		}
	}

	SqlType( int code, String string ) {
		this.code = code;
		this.name = string;
	}

	public int code() {
		return code;
	}

	public static SqlType find( int code )  {
		return codes.get( code );
	}

	public static SqlType find( String name )  {
		if( name == null ) return null;
		return names.get( name.toUpperCase() );
	}

	public static SqlType find( Class<?> klass ) {

		if( klass == null ) return SqlType.NULL;

		if( klass == String.class        ) return SqlType.VARCHAR;
		if( klass == StringBuilder.class ) return SqlType.VARCHAR;
		if( klass == StringBuffer.class  ) return SqlType.VARCHAR;
		if( klass == char.class          ) return SqlType.CHAR;
		if( klass == Character.class     ) return SqlType.CHAR;
		if( klass == boolean.class       ) return SqlType.BOOLEAN;
		if( klass == Boolean.class       ) return SqlType.BOOLEAN;

		if( klass == int.class           ) return SqlType.INTEGER;
		if( klass == Integer.class       ) return SqlType.INTEGER;
		if( klass == double.class        ) return SqlType.DOUBLE;
		if( klass == Double.class        ) return SqlType.DOUBLE;
		if( klass == float.class         ) return SqlType.FLOAT;
		if( klass == Float.class         ) return SqlType.FLOAT;
		if( klass == byte.class          ) return SqlType.TINYINT;
		if( klass == Byte.class          ) return SqlType.TINYINT;
		if( klass == short.class         ) return SqlType.SMALLINT;
		if( klass == Short.class         ) return SqlType.SMALLINT;
		if( klass == long.class          ) return SqlType.BIGINT;
		if( klass == Long.class          ) return SqlType.BIGINT;
		if( klass == BigInteger.class    ) return SqlType.BIGINT;
		if( klass == BigDecimal.class    ) return SqlType.NUMERIC;

		if( klass == byte[].class        ) return SqlType.BLOB;
		if( klass == Byte[].class        ) return SqlType.BLOB;
		if( klass == Date.class          ) return SqlType.DATE;
		if( klass == Calendar.class      ) return SqlType.DATE;
		if( klass == NDate.class         ) return SqlType.DATE;
		if( Types.isMap(klass)           ) return SqlType.JAVA_OBJECT;
		if( Types.isArrayOrCollection(klass) ) return SqlType.ARRAY;

		return SqlType.VARCHAR;

	}

	public String toString() {
		return name;
	}

}