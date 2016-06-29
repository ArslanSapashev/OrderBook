package com.sapashev.storages;

import com.sapashev.Order;
import com.sapashev.interfaces.Addable;
import com.sapashev.interfaces.Operation;
import com.sapashev.interfaces.OrderType;
import com.sapashev.interfaces.Removable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Stores Order objects in LinkedHashMap collection.
 * @author Arslan Sapashev
 * @since 25.06.2016
 * @version 1.0
 */
public class StorageLinkedHashMap implements Addable, Removable, Iterable<Order> {
    LinkedHashMap<Integer, Order> orders = new LinkedHashMap<Integer, Order>();
    private final Logger LOG = LoggerFactory.getLogger(StorageLinkedHashMap.class);

    /**
     * Adds specified order to the collection.
     * @param order
     */
    public void add (Order order) {
        orders.put(order.getOrderID(),order);
    }

    /**
     * Due to multithreading performance optimization, there is no opportunity to delete element of the collection.
     * Instead element to be deleted, will be replaced with new empty one.
     * @param index - ID of order to be deleted.
     * @return
     */
    public boolean remove (int index) {
        boolean isRemoved = false;
        if(orders.put(index,new Order("null", Operation.BUY,0,0,0, OrderType.DELETE)) != null){
            isRemoved = true;
        }
        return isRemoved;
    }

    /**
     * Returns internal iterator.
     * @return - iterator.
     */
    public Iterator<Order> iterator () {
        return new SLHMIterator();
    }

    private class SLHMIterator implements Iterator<Order>{
        Set set = orders.entrySet();
        Iterator iterator = set.iterator();

        /**
         * Returns true if there is another element in collection.
         * @return false - no more elements, true - there is element in collection.
         */
        public boolean hasNext () {
            return iterator.hasNext();
        }

        /**
         * Returns next element of the collection.
         * @return - next element.
         */
        public Order next () {
            Map.Entry entry = (Map.Entry)iterator.next();
            Order order = (Order)entry.getValue();
            //LOG.debug(String.format("Map.Entry return of %s %s",order.getType(),order.getOperation()));
            return order;
        }

        /**
         * Due to multithreading performance optimization, there is no opportunity to delete element of the collection.
         */
        public void remove () {
            return;
        }
    }
}
