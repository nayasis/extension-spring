package com.github.nayasis.spring.extension.query.resolver;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.basica.base.Types;
import com.github.nayasis.basica.model.NDate;
import com.github.nayasis.basica.reflection.Reflector;
import com.github.nayasis.basica.validation.Validator;
import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.ARRAY;
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
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.LIST;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.LONGNVARCHAR;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.LONGVARBINARY;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.NULL;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.NUMERIC;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.REAL;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.SMALLINT;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.TIME;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.TIMESTAMP;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.TINYINT;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.VARCHAR;
import static com.github.nayasis.spring.extension.query.sql.mapper.SqlType.find;

/**
 * Parameter Value to bind in SQL
 *
 * @author nayasis
 */
public class BindParam {

	private String  key;
    private SqlType type;
    private Object  value;
    private boolean out    = false;

    /**
     * Default Constructor
     *
     * @param value value to set
     */
	public BindParam( Object value ) {
		setValue( null, value, null );
	}

    public BindParam( String key, Object value ) {
        setValue( key, value, null );
    }

    public BindParam( String key, Object value, SqlType sqlType ) {
    	setValue( key, value, sqlType );
    }

    public BindParam( String key, Object value, SqlType sqlType, boolean isOutParameter ) {

    	this.key   = key;
    	this.type  = sqlType;
    	this.value = value;
    	this.out   = isOutParameter;

    	setValue( key, value, sqlType );

    }

    public Object getValue() {
        return this.value;
    }

    public SqlType getType() {
        return type;
    }

    public String getKey() {
    	return key;
    }

    public boolean isOut() {
    	return out;
    }

    public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append( "{" ).append( "key:" ).append( key );

		if( type  != null ) sb.append( ", " ).append( "type:" ).append( type );
		if( value != null ) {
			sb.append( ", " ).append( "value:" ).append( value );
			sb.append( ", " ).append( "valueClass:" ).append( value.getClass() );
		}
		if( out == true   ) sb.append( ", " ).append( "out:y" );

		sb.append( "}" );

		return sb.toString();

	}

    private void setValue( String key, Object value, SqlType sqlType ) {

    	this.key   = key;
    	this.type  = sqlType;
    	this.value = value;

        if( value == null ) {
			setNull();
        	return;
        }

		if( isJsonDate(value) ) {
			this.value = value = new NDate( value.toString(), NDate.ISO_8601_FORMAT ).toDate();
		}

		Class<?> klass = value.getClass();

		if( type == null ) {
        	type = find( klass );
		}

		if( type != BLOB && type != BLOB_BOXED ) {
			if( klass.isArray() ) {
				this.value = Arrays.asList( value );
			}
		}

		if( klass == byte[].class ) {
			type = BLOB;
		} else if( klass == Byte[].class ) {
			type = BLOB_BOXED;
		} else if( klass == String.class ) {

			try {

				if( type == INTEGER ) {
					this.value = Types.toInt( value );
				} else if( type == BOOLEAN ) {
					this.value = Types.toBoolean( value );
				} else if( type == BIT ) {
					this.value = Types.toBoolean( value );
				} else if( type == TINYINT ) {
					this.value = Types.toByte( value );
				} else if( type == SMALLINT ) {
					this.value = Types.toShort( value );
				} else if( type == FLOAT ) {
					this.value = Types.toFloat( value );
				} else if( type == DOUBLE ) {
					this.value = Types.toDouble( value );
				} else if( type == DATE || type == TIME || type == TIMESTAMP  ) {
					this.value =  new NDate( (String) value ).toDate();
				} else if( type == REAL || type == DECIMAL || type == NUMERIC  ) {
					this.value = Types.toBigDecimal( value );
				} else if( type == LIST ) {
					this.value = Types.toList( value );
				}

			} catch( NumberFormatException e ) {
				throw new NumberFormatException( Strings.format("parameter casting exception. (key:{},value:{},class:{})", this.key, value, e.getClass().getSimpleName()) );
			}

		} else if( klass == Date.class ) {
			if( type == TIME || type == TIMESTAMP ) return;
			type = DATE;

		} else if( klass == Calendar.class ) {
			this.value = ((Calendar) this.value).getTime();
			if( type == TIME || type == TIMESTAMP ) return;
			type = DATE;

		} else if( klass == NDate.class ) {
			this.value = ( (NDate) this.value ).toDate();
			if( type == TIME || type == TIMESTAMP ) return;
			type = DATE;

		// Databse Native Array Object (Must be defined it's type in database)
		} else if( type == ARRAY ) {
			this.value = value;

		} else if( type == VARCHAR || type == CHAR || type == CLOB || type == LONGVARBINARY || type == LONGNVARCHAR ) {
			if( Types.isPrimitive(value) ) {
				this.value = value;
			} else {
				try {
					this.value = Reflector.toJson( value );
				} catch( Exception e ) {
					this.value = value.toString();
				}
			}
		}

    }

	public static boolean isJsonDate( Object value ) {
		if( ! Types.isStringLike(value) ) return false;
		String val = value.toString();
		return Validator.isMatched( val, "\\d{4}-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]\\.\\d{3}[+-]([0-1][0-9]|2[0-4])[0-5][0-9]" );
	}

    private void setNull() {
    	value = null;
    	type  = NULL;
    }

}