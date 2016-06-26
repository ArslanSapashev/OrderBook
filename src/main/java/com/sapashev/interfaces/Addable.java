package com.sapashev.interfaces;

import com.sapashev.Order;

/**
 * Declares method to add order to storage (could wrap any kind of lists, sets, maps, DB).
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public interface Addable {
    void add (Order order);
}
