package com.sapashev.Storages;

import com.sapashev.Order;
import com.sapashev.interfaces.Addable;
import com.sapashev.interfaces.Operation;
import com.sapashev.interfaces.OrderType;
import com.sapashev.interfaces.Removable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sony on 25.06.2016.
 */
public class StorageLinkedHashMap implements Addable, Removable, Iterable<Order> {
    LinkedHashMap<Integer, Order> orders = new LinkedHashMap<Integer, Order>();

    public void add (Order order) {
        orders.put(order.getOrderID(),order);
    }

    public boolean remove (int index) {
        boolean isRemoved = false;
        if(orders.put(index,new Order("null", Operation.BUY,0,0,0, OrderType.DELETE)) != null){
            isRemoved = true;
        }
        return isRemoved;
    }

    public Iterator<Order> iterator () {
        return new SLHMIterator();
    }

    private class SLHMIterator implements Iterator<Order>{
        Set set = orders.entrySet();
        Iterator iterator = set.iterator();



        public boolean hasNext () {
            return iterator.hasNext();
        }

        public Order next () {
            Map.Entry entry = (Map.Entry)iterator.next();
            Order order = (Order)entry.getValue();
            return order;
        }

        public void remove () {
            return;
        }
    }
}
