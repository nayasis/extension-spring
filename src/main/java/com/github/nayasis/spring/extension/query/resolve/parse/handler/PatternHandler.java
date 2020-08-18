package com.github.nayasis.spring.extension.query.resolve.parse.handler;

@FunctionalInterface
public interface PatternHandler {

    void found( int prev, String pattern, int start, int end );

}
