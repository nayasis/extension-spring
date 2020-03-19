package io.nayasis.spring.extension.sql.entity;

import io.nayasis.basica.base.Types;
import io.nayasis.basica.model.NMap;
import io.nayasis.basica.reflection.Reflector;
import io.nayasis.spring.extension.sql.common.QueryConstant;

import java.util.ArrayList;
import java.util.List;

import static io.nayasis.spring.extension.sql.common.QueryConstant.FOR_EACH_INDEX;

/**
 * Query Parameter
 *
 * @author 1002159
 * @since 2016-01-07
 */
public class QueryParameter extends NMap {

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

        if( value == null || Types.isPrimitive(value) ) {
            source.put( QueryConstant.PARAMETER_SINGLE, value );
        } else {
            source.bind( value );
        }

        putAll( Reflector.merge( source, GlobalQueryParameter.getAll() ) );

        return this;

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
     * @return added index
     */
    public int addForeachIndex( int index ) {
        List<Integer> indices = getForeachIndex();
        indices.add( index );
        return indices.size() - 1;
    }

}
