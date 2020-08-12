package com.github.nayasis.spring.extension.query.resolve.parse;

@FunctionalInterface
public interface ParamHandler {

    String handle( int index, String key, ResolveParam param );

}
