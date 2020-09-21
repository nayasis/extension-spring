package com.github.nayasis.spring.extension.jpa.specification;

import com.github.nayasis.basica.validation.Validator;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import java.time.LocalDateTime;
import java.util.Collection;

public abstract class AbstractSpecification<T> {

    public abstract Specification<T> build();

    protected Specification<T> contains( String key, Collection values ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isNotEmpty(values) ) {
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
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.like( getPath(root, key), "%" + value + "%" );
        };
    }

    protected Specification<T> notLike( String key, Object value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.notLike( getPath(root, key), "%" + value + "%" );
        };
    }

    protected Specification<T> likes( String key, Collection values ) {
        Specification<T> specification = null;
        if( Validator.isNotEmpty(values) ) {
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
        if( Validator.isNotEmpty(values) ) {
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
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.equal( getPath(root, key), value );
        };
    }

    protected Specification<T> lessThan( String key, String value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.lessThan( getPath(root, key), value );
        };
    }

    protected Specification<T> lessThanOrEqual( String key, String value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.lessThanOrEqualTo( getPath(root, key), value );
        };
    }

    protected Specification<T> greaterThan( String key, String value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.greaterThan( getPath(root, key), value );
        };
    }

    protected Specification<T> greaterThanOrEqual( String key, String value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.greaterThanOrEqualTo( getPath(root, key), value );
        };
    }

    protected Specification<T> lessThan( String key, LocalDateTime value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.lessThan( getPath(root, key), value );
        };
    }

    protected Specification<T> lessThanOrEqual( String key, LocalDateTime value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.lessThanOrEqualTo( getPath(root, key), value );
        };
    }

    protected Specification<T> greaterThan( String key, LocalDateTime value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.greaterThan( getPath(root, key), value );
        };
    }

    protected Specification<T> greaterThanOrEqual( String key, LocalDateTime value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.greaterThanOrEqualTo( getPath(root, key), value );
        };
    }

    protected Specification<T> lessThan( String key, Integer value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.lessThan( getPath(root, key), value );
        };
    }

    protected Specification<T> lessThanOrEqual( String key, Integer value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.lessThanOrEqualTo( getPath(root, key), value );
        };
    }

    protected Specification<T> greaterThan( String key, Integer value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.greaterThan( getPath(root, key), value );
        };
    }

    protected Specification<T> greaterThanOrEqual( String key, Integer value ) {
        return (Specification<T>) ( root, query, cb ) -> {
            if( Validator.isEmpty(value) ) return cb.conjunction();
            return cb.greaterThanOrEqualTo( getPath(root, key), value );
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