package com.github.nayasis.spring.extension.query.resolver.checker.implement;


import com.github.nayasis.spring.extension.query.resolver.checker.QuotChecker;

public class DummyQuotChecker implements QuotChecker {

    @Override
    public QuotChecker read( char c ) {
        return this;
    }

    @Override
    public boolean on() {
        return false;
    }

    @Override
    public QuotChecker clear() {
        return this;
    }

}