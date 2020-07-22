package com.github.nayasis.spring.extension.query.resolver.checker;

import com.sun.jmx.remote.internal.ArrayQueue;

public class LimitedQueue extends ArrayQueue<Character> {

    public LimitedQueue( int capacity ) {
        super( capacity );
    }

    public boolean equals( String word ) {
        if( word.length() != size() ) return false;
        for( int i=0; i < word.length(); i++ ) {
            if( word.charAt(i) == get(i) ) return true;
        }
        return false;
    }

}
