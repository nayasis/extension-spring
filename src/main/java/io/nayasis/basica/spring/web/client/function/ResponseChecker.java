package io.nayasis.basica.spring.web.client.function;

@FunctionalInterface
public interface ResponseChecker<T> {
    void check( T response );
}
