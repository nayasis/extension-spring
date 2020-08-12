package com.github.nayasis.spring.extension.query.resolve.parse;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.basica.validation.Assert;
import com.github.nayasis.spring.extension.query.entity.QueryParameter;
import com.github.nayasis.spring.extension.query.resolve.checker.ParamDetector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class QueryResolver {

    private List<String>             queries = new ArrayList<>();
    private List<String>             keys    = new ArrayList<>();
    private Map<String,ResolveParam> params  = new HashMap<>();

    private ParamDetector detector;

    public QueryResolver( ParamDetector detector ) {
        this.detector = detector;
    }

    public String getQuery( ParamHandler handler ) {

        StringBuilder res = new StringBuilder();

        int index = 0;

        for( String query : queries ) {

            res.append( query );

            String       key   = keys.get( index++ );
            ResolveParam param = params.get(key);

            res.append( handler.handle(index,key,param) );

        }

        return Strings.compressEnter( res.toString() );

    }

    public QueryResolver parse( String query, QueryParameter params ) {
        Assert.notEmpty( query, "query is empty." );
        clear();
        parseQuery( dynamicQuery(query,params), params );
        return this;
    }

    private void clear() {
        queries.clear();
        keys.clear();
        params.clear();
    }

    private String dynamicQuery( String query, QueryParameter params ) {

        StringBuilder sql = new StringBuilder();

        int prev = 0;

        while( true ) {

            int start = detector.find( query, "${", prev );
            if( start < 0 ) break;

            int end = getEndIndex( query, '}', start );
            if( end < 0 ) break;

            String keyword = query.substring( start + 2, end );

            ResolveParam param = new ResolveParam(keyword).bindValue(params);

            if( param.valueContains() ) {
                sql.append( query.substring(prev,start) );
                sql.append( param.value() );
            }

            prev = end + 1;

        }

        sql.append( query.substring(prev) );

        return sql.toString();

    }

    private void parseQuery( String query, QueryParameter params ) {

        int prev = 0;

        while( true ) {

            int start = detector.find( query, "#{", prev );
            if( start < 0 ) break;

            int end = getEndIndex( query, '}', start );
            if( end < 0 ) break;

            String keyword = query.substring( start + 2, end );

            ResolveParam param = new ResolveParam(keyword).bindValueOrDefault(params);

            if( param.valueContains() ) {
                this.queries.add( query.substring(prev,start) );
                this.params.put( param.key(), param );
            }

            prev = end + 1;

        }

        queries.add( query.substring(prev) );

    }

    private int getEndIndex( String query, char find, int start ) {
        for( int i = start; i < query.length(); i++ ) {
            if( query.charAt(i) == find ) return i;
        }
        return -1;
    }

}
