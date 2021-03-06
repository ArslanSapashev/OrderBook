package com.sapashev.comparator;

import com.sapashev.Order;

import java.util.Comparator;

/**
 * Sorts Order object by "price" field in ascending manner
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class AscendingPrice implements Comparator<Order> {
    public int compare (Order o1, Order o2) {
        int price1 = o1.getPrice();
        int price2 = o2.getPrice();

        return price1 > price2 ? 1 : (price1 < price2 ? -1 : 0);
    }
}
