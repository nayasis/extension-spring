package com.github.nayasis.spring.extension.query.resolve.checker;

import com.github.nayasis.spring.extension.query.resolve.parse.CircularQueue;

public class ParamDetector {

    private CommentChecker comment;
    private QuotChecker    quot;

    public ParamDetector( CommentChecker comment, QuotChecker quot ) {
        this.comment = comment;
        this.quot    = quot;
    }

    public int find( String query, String word, int start ) {

        comment.clear();
        quot.clear();

        CircularQueue queue = new CircularQueue( word.length() );

        for( int i = start; i < query.length(); i++ ) {

            char c = query.charAt( i );

            queue.enqueue( c );

            if( ! quot.on() && comment.read(c).on() )
                continue;

            if( quot.read(c).on() )
                continue;

            if( queue.equals(word) )
                return i - word.length() + 1;

        }

        return -1;

    }

}