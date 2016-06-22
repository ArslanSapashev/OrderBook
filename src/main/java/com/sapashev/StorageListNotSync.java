package com.sapashev;

import com.sapashev.interfaces.Addable;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores order objects in simple non-synchronized list.
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0 */
public class StorageListNotSync implements Addable{
    List<Order> list = new ArrayList<Order>();

    public void add (Order order) {
        list.add(order);
    }
}
