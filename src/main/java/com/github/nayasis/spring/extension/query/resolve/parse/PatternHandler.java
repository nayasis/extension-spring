package com.github.nayasis.spring.extension.query.resolve.parse;

@FunctionalInterface
public interface PatternHandler {

    void found( int prev, String pattern, int start, int end );

}
