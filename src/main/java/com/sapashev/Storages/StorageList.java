package com.sapashev.Storages;

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

    public void add (Order order) {
        list.add(order);
    }

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
                //order.lock.lock();   НАДО ЧТО ТО ПРИДУМАТЬ ДЛЯ ПАРАЛЛЕЛЬНОЙ ОБРАБОТКИ
            }
            return order;
        }
        public void remove () {
            throw new UnsupportedOperationException("Remove of elements is prohibited");
        }
    }
}
