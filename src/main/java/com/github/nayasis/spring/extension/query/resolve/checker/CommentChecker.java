package com.github.nayasis.spring.extension.query.resolve.checker;

public interface CommentChecker {

    CommentChecker read( char curr );

    boolean on();

    CommentChecker clear();

}
