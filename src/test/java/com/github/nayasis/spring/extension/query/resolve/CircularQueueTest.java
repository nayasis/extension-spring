package com.github.nayasis.spring.extension.query.resolve;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CircularQueueTest {

    @Test
    public void basic() {

        CircularQueue queue = new CircularQueue( 3 );

        for( int i = 0; i < 10; i++ ) {

            Assertions.assertEquals( 0, queue.size() );

            queue.enqueue( 'a' );

            Assertions.assertEquals( 1, queue.size() );

            queue.enqueue( 'b' );

            Assertions.assertEquals( 2, queue.size() );

            queue.enqueue( 'c' );

            Assertions.assertEquals( 3, queue.size() );

            queue.enqueue( 'd' );

            Assertions.assertEquals( 3, queue.size() );

            queue.enqueue( 'e' );

            Assertions.assertEquals( 3, queue.size() );

            Assertions.assertEquals( 'c', queue.get(0) );
            Assertions.assertEquals( 'd', queue.get(1) );
            Assertions.assertEquals( 'e', queue.get(2) );

            Assertions.assertEquals( "cde", new String(queue.toArray()) );
            Assertions.assertTrue( queue.equals("cde") );

            Assertions.assertEquals( 'c', queue.dequeue() );
            Assertions.assertEquals( 2, queue.size() );
            Assertions.assertEquals( "de", new String(queue.toArray()) );
            Assertions.assertEquals( 'd', queue.dequeue() );
            Assertions.assertEquals( 1, queue.size() );
            Assertions.assertEquals( "e", new String(queue.toArray()) );
            Assertions.assertEquals( 'e', queue.dequeue() );
            Assertions.assertEquals( 0, queue.size() );
            Assertions.assertEquals( "", new String(queue.toArray()) );

        }

    }

}