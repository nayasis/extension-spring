package com.github.nayasis.spring.extension.query.resolve.checker.implement;


import com.github.nayasis.spring.extension.query.resolve.checker.QuotChecker;

public class SqlQuotChecker implements QuotChecker {

    private boolean quot = false;

    public SqlQuotChecker read( char c ) {
        if( quot == true ) {
            if( c == '\'' ) quot = false;
        } else {
            if( c == '\'' ) {
                quot = true;
            }
        }
        return this;
    }
    
    public boolean on() {
        return quot;
    }

    public SqlQuotChecker clear() {
        quot       = false;
        return this;
    }

}