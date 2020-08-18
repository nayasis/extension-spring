package com.github.nayasis.spring.extension.query.resolve.parse.handler;

import com.github.nayasis.spring.extension.query.resolve.parse.ResolveParam;

@FunctionalInterface
public interface ParamHandler {

    String handle( int index, String key, ResolveParam param );

}
