package com.sapashev.storages;

import com.sapashev.Order;
import com.sapashev.interfaces.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores Order objects in ArrayList.
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class StorageList implements Addable, Removable, Iterable<Order> {
    List<Order> list = new ArrayList<Order>();

    /**
     * Adds specified order to the collection.
     * @param order
     */
    public void add (Order order) {
        list.add(order);
    }

    /**
     * Due to multithreading performance optimization, there is no opportunity to delete element of the collection.
     * Instead element to be deleted, will be replaced with new empty one.
     * @param orderID - ID of the order to be deleted.
     * @return
     */
    public boolean remove (int orderID) {
        boolean isRemoved = false;
        for(int i = 0; i < list.size()-1;i++){
            if(list.get(i).getOrderID() == orderID){
                list.set(i,new Order("null",Operation.BUY,0,0,0,OrderType.DELETE));
                isRemoved = true;
                break;
            }
        }
        return isRemoved;
    }

    /**
     * Returns internal iterator.
     * @return - iterator.
     */
    public Iterator<Order> iterator () {
        return new StorageIterator();
    }

    public class StorageIterator implements Iterator<Order>{
        int index = 0;

        /**
         * Returns true if there is another element in collection.
         * @return false - no more elements, true - there is element in collection.
         */
        public boolean hasNext () {
            return index < list.size();
        }

        /**
         * Returns next element of the collection.
         * @return - next element.
         */
        public Order next () {
            Order order = null;
            if(index < list.size()){
                order = list.get(index++);
            }
            return order;
        }

        /**
         * Due to multithreading performance optimization, there is no opportunity to delete element of the collection.
         */
        public void remove () {
            throw new UnsupportedOperationException("Remove of elements is prohibited");
        }
    }
}
