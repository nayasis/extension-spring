package com.github.nayasis.spring.extension.jpa.specification;

import com.github.nayasis.basica.model.NDate;
import com.github.nayasis.basica.validation.Validator;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import static com.github.nayasis.basica.validation.Validator.*;

public abstract class AbstractSpecification<T> {

    public abstract Specification<T> build();

    protected Specification<T> contains( String key, Collection values ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( isNotEmpty(values) ) {
                Path<Object> column = getPath( root, key );
                CriteriaBuilder.In<Object> in = cb.in( column );
                values.forEach( status -> {
                    in.value( status );
                });
                return in;
            }
            return cb.conjunction();
        };
    }

    protected Specification<T> like( String key, Object value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( isEmpty(value) ) return cb.conjunction();
            return cb.like( getPath(root, key), "%" + value + "%" );
        };
    }

    protected Specification<T> notLike( String key, Object value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( isEmpty(value) ) return cb.conjunction();
            return cb.notLike( getPath(root, key), "%" + value + "%" );
        };
    }

    protected Specification<T> likes( String key, Collection values ) {
        Specification<T> specification = null;
        if( isNotEmpty(values) ) {
            for( Object value : values ) {
                if( value == null ) continue;
                if( specification == null ) {
                    specification = like( key, value );
                } else {
                    specification = specification.or( like( key,value ) );
                }
            }
        }
        return specification;
    }

    protected Specification<T> notLikes( String key, Collection values ) {
        Specification<T> specification = null;
        if( isNotEmpty(values) ) {
            for( Object value : values ) {
                if( value == null ) continue;
                if( specification == null ) {
                    specification = notLike( key, value );
                } else {
                    specification = specification.or( notLike( key,value ) );
                }
            }
        }
        return specification;
    }

    protected Specification<T> isNull( String key ) {
        return (Specification<T>) ( root, query, cb ) -> {
            return cb.isNull( getPath(root, key) );
        };
    }

    protected Specification<T> isNotNull( String key ) {
        return (Specification<T>) ( root, query, cb ) -> {
            return cb.isNotNull( getPath(root, key) );
        };
    }

    protected Specification<T> equal( String key, Object value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( isEmpty(value) ) return cb.conjunction();
            return cb.equal( getPath(root, key), value );
        };
    }

    protected <E> Specification<T> between( String key, E from, E to ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( isEmpty(from) || isEmpty(to) ) return cb.conjunction();
            Path path = getPath(root, key);

            if( from instanceof Integer )
                return cb.between( path, (Integer) from, (Integer) to );
            if( from instanceof Double )
                return cb.between( path, (Double) from, (Double) to );
            if( from instanceof Float )
                return cb.between( path, (Float) from, (Float) to );
            if( from instanceof BigDecimal )
                return cb.between( path, (BigDecimal) from, (BigDecimal) to );
            if( from instanceof BigInteger )
                return cb.between( path, (BigInteger) from, (BigInteger) to );
            if( from instanceof LocalDateTime )
                return cb.between( path, (LocalDateTime) from, (LocalDateTime) to );
            if( from instanceof LocalDate )
                return cb.between( path, (LocalDate) from, (LocalDate) to );
            if( from instanceof NDate )
                return cb.between( path, ((NDate) from).toLocalDateTime(), ((NDate) to).toLocalDateTime() );

            return cb.between( path, from.toString(), to.toString() );
        };
    }

    protected Specification<T> lessThan( String key, Object value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( isEmpty(value) ) return cb.conjunction();
            Path path = getPath(root, key);

            if( value instanceof Integer )
                return cb.lessThan( path, (Integer) value );
            if( value instanceof Double )
                return cb.lessThan( path, (Double) value );
            if( value instanceof Float )
                return cb.lessThan( path, (Float) value );
            if( value instanceof BigDecimal )
                return cb.lessThan( path, (BigDecimal) value );
            if( value instanceof BigInteger )
                return cb.lessThan( path, (BigInteger) value );
            if( value instanceof LocalDateTime )
                return cb.lessThan( path, (LocalDateTime) value );
            if( value instanceof LocalDate )
                return cb.lessThan( path, (LocalDate) value );
            if( value instanceof NDate )
                return cb.lessThan( path, ((NDate) value).toLocalDateTime() );

            return cb.lessThan( path, value.toString() );
        };
    }

    protected Specification<T> lessThanOrEqual( String key, Object value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( isEmpty(value) ) return cb.conjunction();
            Path path = getPath(root, key);

            if( value instanceof Integer )
                return cb.lessThanOrEqualTo( path, (Integer) value );
            if( value instanceof Double )
                return cb.lessThanOrEqualTo( path, (Double) value );
            if( value instanceof Float )
                return cb.lessThanOrEqualTo( path, (Float) value );
            if( value instanceof BigDecimal )
                return cb.lessThanOrEqualTo( path, (BigDecimal) value );
            if( value instanceof BigInteger )
                return cb.lessThanOrEqualTo( path, (BigInteger) value );
            if( value instanceof LocalDateTime )
                return cb.lessThanOrEqualTo( path, (LocalDateTime) value );
            if( value instanceof LocalDate )
                return cb.lessThanOrEqualTo( path, (LocalDate) value );
            if( value instanceof NDate )
                return cb.lessThanOrEqualTo( path, ((NDate) value).toLocalDateTime() );

            return cb.lessThanOrEqualTo( path, value.toString() );
        };
    }

    protected Specification<T> greaterThan( String key, Object value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( isEmpty(value) ) return cb.conjunction();
            Path path = getPath(root, key);

            if( value instanceof Integer )
                return cb.greaterThan( path, (Integer) value );
            if( value instanceof Double )
                return cb.greaterThan( path, (Double) value );
            if( value instanceof Float )
                return cb.greaterThan( path, (Float) value );
            if( value instanceof BigDecimal )
                return cb.greaterThan( path, (BigDecimal) value );
            if( value instanceof BigInteger )
                return cb.greaterThan( path, (BigInteger) value );
            if( value instanceof LocalDateTime )
                return cb.greaterThan( path, (LocalDateTime) value );
            if( value instanceof LocalDate )
                return cb.greaterThan( path, (LocalDate) value );
            if( value instanceof NDate )
                return cb.greaterThan( path, ((NDate) value).toLocalDateTime() );

            return cb.greaterThan( path, value.toString() );
        };
    }

    protected Specification<T> greaterThanOrEqual( String key, Object value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( isEmpty(value) ) return cb.conjunction();
            Path path = getPath(root, key);

            if( value instanceof Integer )
                return cb.greaterThanOrEqualTo( path, (Integer) value );
            if( value instanceof Double )
                return cb.greaterThanOrEqualTo( path, (Double) value );
            if( value instanceof Float )
                return cb.greaterThanOrEqualTo( path, (Float) value );
            if( value instanceof BigDecimal )
                return cb.greaterThanOrEqualTo( path, (BigDecimal) value );
            if( value instanceof BigInteger )
                return cb.greaterThanOrEqualTo( path, (BigInteger) value );
            if( value instanceof LocalDateTime )
                return cb.greaterThanOrEqualTo( path, (LocalDateTime) value );
            if( value instanceof LocalDate )
                return cb.greaterThanOrEqualTo( path, (LocalDate) value );
            if( value instanceof NDate )
                return cb.greaterThanOrEqualTo( path, ((NDate) value).toLocalDateTime() );

            return cb.greaterThanOrEqualTo( path, value.toString() );
        };
    }

    protected Specification<T> junction( Specification specification ) {
        return (Specification<T>) specification;
    }

    protected Path getPath( Path expression, String key ) {

        if( expression == null ) return null;

        int index = key.indexOf( "." );

        if( index < 0 ) {
            return expression.get( key );
        } else {

            String prevKey = key.substring( 0, index );
            String nextKey = key.substring( index + 1 );

            Path path = expression.get( prevKey );

            Path child = getPath( path, nextKey );

            if( child != null ) {
                return child;
            } else {
                return path;
            }

        }

    }

}