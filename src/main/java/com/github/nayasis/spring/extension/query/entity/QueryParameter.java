package com.github.nayasis.spring.extension.query.entity;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.basica.base.Types;
import com.github.nayasis.basica.model.NMap;
import com.github.nayasis.basica.reflection.Reflector;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.github.nayasis.spring.extension.query.common.QueryConstant.FOR_EACH_INDEX;
import static com.github.nayasis.spring.extension.query.common.QueryConstant.PARAMETER_INNER_FOR_EACH;
import static com.github.nayasis.spring.extension.query.common.QueryConstant.PARAMETER_SINGLE;

/**
 * Query Parameter
 *
 * @author 1002159
 * @since 2016-01-07
 */
@NoArgsConstructor
public class QueryParameter extends NMap<String,Object> {

    public QueryParameter( Object value ) {
        init( value );
    }

    /**
     * Init Query Parameter from value
     *
     * @param value single value (primitive or array or list) or Map
     * @return self instance
     */
    private void init( Object value ) {

        putAll( Reflector.toMapFrom(ThreadlocalQueryParameter.getAll()) );

        if( value == null || isPrimitive( value ) ) {
            put( PARAMETER_SINGLE, value );
        } else {
            bind( value );
        }

        put( FOR_EACH_INDEX, new ArrayList<Integer>() );

    }

    private boolean isPrimitive( Object value ) {
        if( Types.isImmutable(value) ) return true;
        if( Types.isArrayOrCollection(value) ) return true;
        return false;
    }

    public boolean hasSingleParameter() {
        return containsKey(PARAMETER_SINGLE);
    }

    public Object getSingleParameter() {
        return get(PARAMETER_SINGLE);
    }

    public List<Integer> getForeachIndex() {
        return (List<Integer>) get(FOR_EACH_INDEX);
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

    public QueryParameter clone() {
        QueryParameter clone = new QueryParameter();
        clone.putAll( this );
        return clone;
    }

    public boolean hasForEachInnerParam() {
        return containsKey( PARAMETER_INNER_FOR_EACH );
    }

    public NMap getForEachInnerParam() {
        if( hasForEachInnerParam() ) {
            return (NMap) get( PARAMETER_INNER_FOR_EACH );
        } else {
            return null;
        }
    }

    public Object getForEachInnerParam( String key ) {
        NMap map = getForEachInnerParam();
        if( map == null ) return null;
        return map.getByPath( readKey(key) );
    }

    /**
     * Set inner parameter to be used only in query assembling of SqlNode
     * @param key   key
     * @param value value
     * @return self instance
     */
    public QueryParameter setForEachInnerParam( String key, Object value ) {

        if( ! hasForEachInnerParam() ) {
            put( PARAMETER_INNER_FOR_EACH, new NMap() );
        }

        getForEachInnerParam().put( writeKey(key), value );

        return this;

    }

    private String readKey( String key ) {

        List<String> tokens = Strings.tokenize( key, "." );

        if( tokens.isEmpty() ) return "";
        if( tokens.size() == 1 ) return tokens.get(0);

        StringBuilder sb = new StringBuilder();

        sb.append( tokens.get(0) );
        for( int i = 1; i < tokens.size() - 1; i++ ) {
            sb.append('_').append( tokens.get(i) );
        }
        sb.append( '.' ).append( tokens.get(tokens.size() - 1) );

        return sb.toString();

    }

    private String writeKey( String key ) {
        return key.replace(".", "_" );
    }

}