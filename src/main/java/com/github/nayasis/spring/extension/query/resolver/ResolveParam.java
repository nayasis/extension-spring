package com.github.nayasis.spring.extension.query.resolver;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.basica.model.NMap;
import com.github.nayasis.spring.extension.query.entity.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent=true)
public class ResolveParam {

    private String  key;
    private String  type;
    private boolean out;
    private Object  value;
    private boolean valueContains;

    public ResolveParam( String keyword ) {

        List<String> words = Strings.tokenize(keyword, ":");

        out = hasOut(words);

        switch( words.size() ) {
            case 0 :
                return;
            case 1 :
                key  = words.get(0);
                break;
            default :
                key  = words.get(0);
                type = words.get(1);
        }

    }

    public ResolveParam bindValueOrDefault( QueryParameter params ) {
        if( key != null ) {
            valueContains = params.containsByPath( key );
            if( ! valueContains ) {
                if( params.hasSingleParameter() ) {
                    valueContains = true;
                    value = params.getSingleParameter();
                }
            } else {
                value = params.getByPath( key );
            }
        }
        return this;
    }

    public ResolveParam bindValue( NMap params ) {
        if( key != null ) {
            valueContains = params.containsByPath( key );
            if( valueContains ) {
                value = params.getByPath( key );
            }
        }
        return this;
    }

    private boolean hasOut( List<String> words ) {
        for( int i = words.size() - 1; i > 0; i-- ) {
            String word = words.get(i).toLowerCase();
            if( "in".equals(word) ) {
                words.remove(i);
                return false;
            } else if( "out".equals(word) ) {
                words.remove(i);
                return true;
            }
        }
        return false;
    }

}
