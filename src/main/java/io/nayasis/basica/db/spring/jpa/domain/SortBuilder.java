package io.nayasis.basica.db.spring.jpa.domain;

import io.nayasis.basica.base.Strings;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

import static io.nayasis.basica.base.Strings.isEmpty;

/**
 * JPA Sort builder
 */
@UtilityClass
public class SortBuilder {

    /**
     * build sort rule
     *
     * @param rule  sort rule like "colA,direction1 &amp; colB direction2 &amp; ..."
     *              ex. name,asc &amp; id,desc
     * @return query sorting rule
     */
    public Sort toSort( String rule ) {
        return toSort( rule, null );
    }


    /**
     * build sort rule
     *
     * @param rule  sort rule like "colA,direction1 &amp; colB direction2 &amp; ..."
     *              ex. name,asc &amp; id,desc
     * @param defaultRule   default sort rule if rule is empty
     * @return query sorting rule
     */
    public Sort toSort( String rule, String defaultRule ) {
        List<Order> orders = toOrders( rule );
        if( isEmpty(rule) )
            orders = toOrders( defaultRule );
        return Sort.by( orders );
    }

    /**
     * build order rules
     *
     * @param rule order creation rule like colA,direction1 &amp; colB direction2 &amp; ..."
     *             ex. name,asc &amp; id,desc
     * @return multiple orders
     */
    public List<Order> toOrders( String rule ) {
        List<Order> orders = new ArrayList<>();
        for( String param : Strings.split(rule, "&") ) {
            Order order = toOrder(param);
            if (order == null) continue;
            orders.add(order);
        }
        return orders;
    }

    /**
     * build order
     *
     * @param rule order rule like "column,direction"
     *             ex1. colA,desc
     *             ex2. colA,asc
     *             ex3. colA
     * @return order
     */
    public Order toOrder( String rule ) {

        List<String> words = Strings.split( rule, "," );

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
