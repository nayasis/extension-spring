package com.github.nayasis.spring.extension.query.resolve.checker;

import com.github.nayasis.basica.validation.Validator;

public class CircularQueue {

    private int    length;
    private char[] queue;
    private int    head = 0;
    private int    tail = 0;

    public CircularQueue( int capacity ) {
        this.length = capacity + 1;
        this.queue  = new char[ capacity + 1 ];
    }

    public void enqueue( char c ) {
        queue[ tail ] = c;
        tail = ( tail + 1 ) % length;
        if( tail == head ) {
            head = index(1);
        }
    }

    public char dequeue() {
        if( head == tail )
            throw new IndexOutOfBoundsException("empty queue");
        char removed = queue[head];
        head = index(1);
        return removed;
    }

    public char get( int i ) {
        return queue[ index(i) ];
    }

    private int index( int i ) {
        if( i < 0 ) {
            int capacity = length - 1;
            return index( (size() + (i % capacity)) % capacity );
        } else {
            return ( head + i ) % length;
        }
    }

    public int size() {
        int diff = tail - head;
        if( diff < 0 )
            diff += length;
        return diff;
    }

    public char[] toArray() {
        int size = size();
        char[] array = new char[size];
        for( int i = 0; i < size; i++ ) {
            array[i] = queue[index(i)];
        }
        return array;
    }

    public boolean equals( String word ) {
        if( word.length() != size() ) return false;
        return Validator.isEqual( word.toCharArray(), toArray() );
    }

}
