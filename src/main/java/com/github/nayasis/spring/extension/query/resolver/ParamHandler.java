package com.github.nayasis.spring.extension.query.resolver;

@FunctionalInterface
public interface ParamHandler {

    String handle( int index, String key, ResolveParam param );

}
