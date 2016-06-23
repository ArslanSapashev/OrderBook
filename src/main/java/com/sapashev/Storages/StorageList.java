package com.sapashev.Storages;

import com.sapashev.Order;
import com.sapashev.interfaces.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores order objects in simple non-synchronized list.
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class StorageList implements Addable, Removable, Iterable<Order> {
    List<Order> list = new ArrayList<Order>();

    public void add (Order order) {
        list.add(order);
    }

    public boolean remove (int index) {
        boolean isRemoved = false;
        boolean isOrderLocked = false;
        if(index >=0 && index < list.size()){
            try{
                if(list.get(index).lock.tryLock()){
                    isOrderLocked = true;
                    list.set(index, new Order("null",Operation.BUY,0,0,0,OrderType.ADD));
                    isRemoved = true;
                }
            }
            finally {
                if(isOrderLocked){
                    list.get(index).lock.unlock();
                }
            }
        }
        return isRemoved;
    }

    public Iterator<Order> iterator () {
        return new StorageIterator();
    }

    public class StorageIterator implements Iterator<Order>{
        int index = 0;

        public boolean hasNext () {
            return index < list.size();
        }

        public Order next () {
            Order order = null;
            if(index < list.size()){
                order = list.get(index++);
                order.lock.lock();
            }
            return order;
        }

        public void remove () {
            throw new UnsupportedOperationException("Remove of elements is prohibited");
        }
    }
}
