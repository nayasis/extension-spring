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
public class QueryResolver<T> {

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
            if( query == null ) {
                String       key   = keys.get( index++ );
                ResolveParam param = params.get(key);
                res.append( handler.handle(index,key,param) );
            } else {
                res.append( query );
            }
        }

        return Strings.compressEnter( res.toString() );

    }

    public T parse( String query, QueryParameter params ) {
        Assert.notEmpty( query, "query is empty." );
        clear();
        parseQuery( dynamicQuery(query,params), params );
        return (T) this;
    }

    private void clear() {
        queries.clear();
        keys.clear();
        params.clear();
    }

    public String dynamicQuery( String query, QueryParameter params ) {

        StringBuilder sql = new StringBuilder();

        parse(query, "${", "}", ( prev, keyword, start, end ) -> {

            ResolveParam param = new ResolveParam(keyword).bindValue(params);

            if( param.valueContains() ) {
                sql.append( query.substring(prev,start) );
                sql.append( param.value() );
            } else {
                sql.append( query.substring(prev,end) );
            }

        }, remain -> {
            sql.append( remain );
        });

        return sql.toString();

    }

    public void parseQuery( String query, QueryParameter params ) {

        parse(query, "#{", "}", ( prev, keyword, start, end ) -> {

            ResolveParam param = new ResolveParam(keyword).bindValueOrDefault(params);

            if( param.valueContains() ) {
                queries.add( query.substring(prev,start) );
                queries.add( null );
                keys.add( param.key() );
                this.params.put( param.key(), param );
            } else {
                queries.add( query.substring(prev,end) );
            }

        }, remain -> {
            queries.add( remain );
        });

    }

    public void parse( String query, String startPattern, String endPattern, PatternHandler patternHandler, RemainHandler remainHandler ) {

        int prev = 0;

        while( true ) {

            int start = detector.find( query, startPattern, prev );
            if( start < 0 ) break;

            int end = detector.find( query, endPattern, start );
            if( end < 0 ) break;

            String keyword = query.substring( start + startPattern.length(), end );

            patternHandler.found( prev, keyword, start, end + 1 );

            prev = end + 1;

        }

        String remain = query.substring(prev);

        if( ! remain.isEmpty() )
            remainHandler.found( remain );

    }

}
