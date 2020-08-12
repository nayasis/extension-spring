package com.github.nayasis.spring.extension.query.resolve.checker.implement;


public class UnifiedQuotChecker {

    private boolean quot       = false;
    private boolean doubleQuot = false;
    
    public UnifiedQuotChecker read( char c ) {
        if( quot == true ) {
            if( c == '\'' ) quot = false;
        } else if( doubleQuot == true ) {
            if( c == '"' ) doubleQuot = false;
        } else {
            if( c == '\'' ) {
                quot = true;
            } else if( c == '"' ) {
                doubleQuot = true;
            }
        }
        return this;
    }
    
    public boolean on() {
        return quot || doubleQuot;
    }

    public UnifiedQuotChecker clear() {
        quot       = false;
        doubleQuot = false;
        return this;
    }

}
