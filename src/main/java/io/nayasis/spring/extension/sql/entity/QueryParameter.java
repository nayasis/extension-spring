package io.nayasis.spring.extension.sql.entity;

import io.nayasis.basica.base.Types;
import io.nayasis.basica.model.NMap;
import io.nayasis.basica.reflection.Reflector;

import java.util.ArrayList;
import java.util.List;

import static io.nayasis.spring.extension.sql.common.QueryConstant.FOR_EACH_INDEX;
import static io.nayasis.spring.extension.sql.common.QueryConstant.PARAMETER_SINGLE;

/**
 * Query Parameter
 *
 * @author 1002159
 * @since 2016-01-07
 */
public class QueryParameter extends NMap<String,Object> {

    public QueryParameter() {}

    public QueryParameter( Object value ) {
        init( value );
    }

    /**
     * Init Query Parameter from value
     *
     * @param value single value (primitive or array or list) or Map
     * @return self instance
     */
    public QueryParameter init( Object value ) {

        clear();

        NMap source = new NMap();

        if( value == null || isPrimitive( value ) ) {
            source.put( PARAMETER_SINGLE, value );
        } else {
            source.bind( value );
        }

        putAll( Reflector.merge( source, GlobalQueryParameter.getAll() ) );

        return this;

    }

    private boolean isPrimitive( Object value ) {
        if( Types.isImmutable(value) ) return true;
        if( Types.isArrayOrCollection(value) ) return true;
        return false;
    }

    public boolean hasSingleParameter() {
        return containsKey( PARAMETER_SINGLE );
    }

    public Object getSingleParameter() {
        return get( PARAMETER_SINGLE );
    }

    public List<Integer> getForeachIndex() {
        if( ! containsKey( FOR_EACH_INDEX ) ) {
            put( FOR_EACH_INDEX, new ArrayList<Integer>() );
        }
        return (List<Integer>) get( FOR_EACH_INDEX );
    }

    /**
     * add ForEach index
     *
     * @return added element's index
     */
    public int addForeachIndex( int index ) {
        List<Integer> indices = getForeachIndex();
        indices.add( index );
        return indices.size() - 1;
    }

}
