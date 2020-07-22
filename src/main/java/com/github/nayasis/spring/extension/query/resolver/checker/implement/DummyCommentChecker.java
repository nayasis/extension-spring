package com.github.nayasis.spring.extension.query.resolver.checker.implement;

import com.github.nayasis.spring.extension.query.resolver.checker.CommentChecker;

public class DummyCommentChecker implements CommentChecker {

    @Override
    public CommentChecker read( char curr ) {
        return this;
    }

    @Override
    public boolean on() {
        return false;
    }

    @Override
    public CommentChecker clear() {
        return this;
    }

}
