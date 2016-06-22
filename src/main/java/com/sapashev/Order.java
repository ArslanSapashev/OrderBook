package com.sapashev;

import com.sapashev.interfaces.OrderType;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Describes trade order
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class Order {
    private final OrderType type;
    private final String book;
    private final Operation operation;
    private final int price;
    private final int volume;
    private final int orderID;
    public volatile ReentrantLock lock = new ReentrantLock();

    public Order (String book, Operation operation, int price, int volume, int orderID, OrderType type) {
        this.book = book;
        this.operation = operation;
        this.price = price;
        this.volume = volume;
        this.orderID = orderID;
        this.type = type;
    }

    public String getBook () {
        return book;
    }

    public Operation getOperation () {
        return operation;
    }

    public int getPrice () {
        return price;
    }

    public int getVolume () {
        return volume;
    }

    public int getOrderID () {
        return orderID;
    }

    public OrderType getType () {
        return type;
    }

    public enum Operation {
        BUY,SELL;
    }
}
