package com.github.nayasis.spring.extension.web.client.function;

@FunctionalInterface
public interface ResponseChecker<T> {
    void check( T response );
}
