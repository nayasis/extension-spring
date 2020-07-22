package com.github.nayasis.spring.extension.query.resolver.checker;

public class ParamDetector {

    private CommentChecker comment;
    private QuotChecker    quot;

    public ParamDetector( CommentChecker comment, QuotChecker quot ) {
        this.comment = comment;
        this.quot    = quot;
    }

    public int find( String sql, String word, int start ) {

        comment.clear();
        quot.clear();

        LimitedQueue queue = new LimitedQueue( word.length() );

        for( int i = start; i < sql.length(); i++ ) {

            char c = sql.charAt( i );

            queue.add( c );

            if( ! quot.on() && comment.read(c).on() )
                continue;

            if( quot.read(c).on() )
                continue;

            if( queue.equals(word) )
                return i - word.length() - 1;

        }

        return -1;

    }

}