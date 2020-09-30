package com.github.nayasis.spring.extension.jpa.domain;

import com.github.nayasis.basica.base.Strings;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

import static com.github.nayasis.basica.base.Strings.isEmpty;

/**
 * JPA Sort builder
 */
@UtilityClass
public class SortBuilder {

    /**
     * build sort expression
     *
     * @param expression  sort expression like "colA,direction1 ^ colB direction2 ^ ..."
     *              ex. name,asc ^ id,desc
     * @return query sorting expression
     */
    public Sort toSort( String expression ) {
        return toSort( expression, null );
    }


    /**
     * build sort expression
     *
     * @param expression  sort expression like "colA,direction1 ^ colB direction2 ^ ..."
     *              ex. name,asc ^ id,desc
     * @param defaultExpression   default sort expression if expression is empty
     * @return query sorting expression
     */
    public Sort toSort( String expression, String defaultExpression ) {
        List<Order> orders = toOrders( expression );
        if( isEmpty(expression) )
            orders = toOrders( defaultExpression );
        return Sort.by( orders );
    }

    /**
     * build order rules
     *
     * @param expression order creation expression like colA,direction1 ^ colB direction2 ^ ..."
     *             ex. name,asc ^ id,desc
     * @return multiple orders
     */
    public List<Order> toOrders( String expression ) {
        List<Order> orders = new ArrayList<>();
        for( String param : Strings.split(expression, "^") ) {
            Order order = toOrder(param);
            if (order == null) continue;
            orders.add(order);
        }
        return orders;
    }

    /**
     * build order
     *
     * @param expression order expression like "column,direction"
     *             ex1. colA,desc
     *             ex2. colA,asc
     *             ex3. colA
     * @return order
     */
    public Order toOrder( String expression ) {

        List<String> words = Strings.split( expression, "," );

        String column    = words.size() > 0 ? words.get(0) : "";
        String direction = words.size() > 1 ? words.get(1) : "";

        if( isEmpty(column) ) return null;

        return new Order( toDirection(direction), column );

    }

    private Direction toDirection( String direction ) {
        switch ( Strings.toLowerCase(direction) ) {
            case "desc" :
                return Direction.DESC;
            case "asc" :
                return Direction.ASC;
            default :
                return Sort.DEFAULT_DIRECTION;
        }
    }

}
