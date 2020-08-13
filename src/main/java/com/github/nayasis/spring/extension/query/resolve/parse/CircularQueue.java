package com.github.nayasis.spring.extension.query.resolve.parse;

import com.github.nayasis.basica.validation.Validator;

public class CircularQueue {

    private int    capacity;
    private char[] queue;
    private int    head = 0;
    private int    tail = 0;

    public CircularQueue( int capacity ) {
        this.capacity = capacity + 1;
        this.queue    = new char[ capacity + 1 ];
    }

    public void enqueue( char c ) {
        queue[ tail ] = c;
        tail = ( tail + 1 ) % capacity;
        if( tail == head ) {
            head = ( head + 1 ) % capacity;
        }
    }

    public char dequeue() {
        if( head == tail )
            throw new IndexOutOfBoundsException("empty queue");
        char removed = queue[head];
        head = ( head + 1 ) % capacity;
        return removed;
    }

    public char get( int i ) {
        int size = size();
        if( i < 0 || i >= size() )
            throw new IndexOutOfBoundsException( String.format("Index %d, queue size %d", i, size) );
        int idx = ( head + i ) % capacity;
        return queue[ idx ];
    }

    public int size() {
        int diff = tail - head;
        if( diff < 0 )
            diff += capacity;
        return diff;
    }

    public char[] toArray() {
        int size = size();
        char[] array = new char[size];
        for( int i = 0; i < size; i++ ) {
            int idx = ( head + i ) % capacity;
            array[i] = queue[idx];
        }
        return array;
    }

    public boolean equals( String word ) {
        if( word.length() != size() ) return false;
        return Validator.isEqual( word.toCharArray(), toArray() );
    }

}
