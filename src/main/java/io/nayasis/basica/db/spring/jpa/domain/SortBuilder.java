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
     * @param rule  sort rule like "key1,direction1 & key2, direction2 & ..."
     *              ex. name,asc & id,desc
     * @return query sorting rule
     */
    public Sort build( String rule ) {
        return build( rule, null );
    }


    /**
     * build sort rule
     *
     * @param rule  sort rule like "key1,direction1 & key2, direction2 & ..."
     *              ex. name,asc & id,desc
     * @param defaultRule   default sort rule if rule is empty
     * @return query sorting rule
     */
    public Sort build( String rule, String defaultRule ) {
        List<Order> orders = toOrders( rule );
        if( isEmpty(rule) )
            orders = toOrders( defaultRule );
        return Sort.by( orders );
    }

    private static List<Order> toOrders( String rule ) {
        List<Order> orders = new ArrayList<>();
        for (String param : Strings.split(rule, "&")) {
            Order order = toOrder(param);
            if (order == null) continue;
            orders.add(order);
        }
        return orders;
    }

    public Order toOrder( String param ) {

        List<String> words = Strings.split( param, "," );

        String col = words.size() > 0 ? words.get(0) : "";
        String dir = words.size() > 1 ? words.get(1) : "";

        if( isEmpty(col) ) return null;

        Direction direction = dir.equalsIgnoreCase("desc") ? Direction.DESC : Direction.ASC ;

        return new Order( direction, col );

    }

}
