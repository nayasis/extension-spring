package com.github.nayasis.spring.extension.query.resolve.checker;


public interface QuotChecker {

    QuotChecker read( char c );
    
    boolean on();

    QuotChecker clear();

}
