package com.sapashev;

import com.sapashev.interfaces.Getable;
import com.sapashev.interfaces.Operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Contains all Order objects which "book" field equals to "bookName" of the OrderBookProcessor object.
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class OrderBookProcessor implements Runnable {
    private final String bookName;
    private final Iterable<Order> orders;
    private final List<Order> bid = new ArrayList<Order>();
    private final List<Order> ask = new ArrayList<Order>();

    public OrderBookProcessor (String bookName, Iterable<Order> orders) {
        this.bookName = bookName;
        this.orders = orders;
    }

    private void fill(){
        Iterator<Order> iterator = orders.iterator();
        if(iterator.hasNext()){
            Order order = iterator.next();
            if (this.bookName.equals(order.getBook())){
                switch (order.getOperation()){
                    case BUY:
                        bid.add(order);
                        break;
                    case SELL:
                        ask.add(order);
                        break;
                }
            }
        }
    }

    private void matchBuyOrder(Order order){
        Iterator<Order> iterator = ask.iterator();

    }

    public void run () {

    }
}
