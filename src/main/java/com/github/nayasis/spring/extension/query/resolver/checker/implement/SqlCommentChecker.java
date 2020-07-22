package com.github.nayasis.spring.extension.query.resolver.checker.implement;

import com.github.nayasis.spring.extension.query.resolver.checker.CommentChecker;

public class SqlCommentChecker implements CommentChecker {

    private enum Status { NONE, BLOCK, LINE }

    private char   prev   = '\u0000';
    private Status status = Status.NONE;

    public SqlCommentChecker read( char curr ) {
        if( prev != '\u0000' ) {
            switch ( status ) {
                case NONE :
                    if( prev == '/' ) {
                        if( curr == '*' )
                            status = Status.BLOCK;
                    } else if( prev == '-' ) {
                        if( curr == '-' )
                            status = Status.LINE;
                    }
                    break;
                case BLOCK :
                    if( prev == '*' && curr == '/' ) {
                        status = Status.NONE;
                    }
                    break;
                case LINE :
                    if( curr == '\n' ) {
                        status = Status.NONE;
                    }
                    break;
            }
        }
        prev = curr;
        return this;
    }

    public boolean on() {
        return status != Status.NONE;
    }

    public SqlCommentChecker clear() {
        prev   = '\u0000';
        status = Status.NONE;
        return this;
    }

}
