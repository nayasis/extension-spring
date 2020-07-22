package com.github.nayasis.spring.extension.query.resolver.checker;

public interface CommentChecker {

    CommentChecker read( char curr );

    boolean on();

    CommentChecker clear();

}
