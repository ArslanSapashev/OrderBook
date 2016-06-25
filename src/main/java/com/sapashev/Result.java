package com.sapashev;

import java.util.List;

/**
 * Created by Sony on 25.06.2016.
 */
public class Result {
    private String book;
    private List<Order> bid;
    private List<Order> ask;

    public Result (String book, List<Order> bid, List<Order> ask) {
        this.book = book;
        this.bid = bid;
        this.ask = ask;
    }

    public String getBook () {
        return book;
    }

    public List<Order> getBid () {
        return bid;
    }

    public List<Order> getAsk () {
        return ask;
    }
}
