package com.sapashev;

import com.sapashev.comparator.AscendingPrice;
import com.sapashev.comparator.DescendingPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Contains all Order objects which "book" field equals to "book" of the OrderBookProcessor object.
 * That class passes through common storage and retrieves orders with matching book.
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class OrderBookProcessor implements Runnable {
    private final String book;
    private final Iterable<Order> orders;
    private final List<Order> bid = new ArrayList<Order>();
    private final List<Order> ask = new ArrayList<Order>();
    private final List<Result> results;
    private final Logger LOG = LoggerFactory.getLogger(OrderBookProcessor.class);

    public OrderBookProcessor (String bookName, Iterable<Order> orders, List<Result> results) {
        this.book = bookName;
        this.orders = orders;
        this.results = results;
    }

    public void run () {
        fillBidAskLists();
        results.add(new Result(book,bid,ask));
    }

    /**
     * Retrieves orders from common storage one by one
     * Checks if order "book" field matches "book" field of this object, and if matches:
     * checks if that order could be executed (order price overlaps the best price of opposite ladder)
     * if they overlaps, order volume will be subtracted from overlapped price volume.
     * That process repeats recursively until volume opposite orders with overlapped price will be exhausted.
     * If after that order's volume will still be more than zero,
     * it would be placed to corresponding ladder (bid/ask lists).
     */
    private void fillBidAskLists (){
        LOG.debug(String.format("%s begins at %d%n", book,System.currentTimeMillis()));
        Iterator<Order> iterator = orders.iterator();
        while (iterator.hasNext()){
            Order order = iterator.next();
            if (this.book.equals(order.getBook())){
                switch (order.getOperation()){
                    case BUY:
                        matchBuyOrder(order);
                        this.sortBID();
                        break;
                    case SELL:
                        matchSellOrder(order);
                        this.sortASK();
                        break;
                }
            }
        }
        LOG.debug(String.format("%s ends at %d%n", book, System.currentTimeMillis()));
    }

    /**
     * Sorts bid list in descending manner.
     */
    private void sortBID(){
        Collections.sort(bid,new DescendingPrice());
    }

    /**
     * Sorts ask list in ascending manner.
     */
    private void sortASK(){
        Collections.sort(ask, new AscendingPrice());
    }

    /**
     * WARNING!!! That method must be invoked ONLY on ask list sorted at ascending manner.
     * Checks if that order could be executed (order price overlaps the best price of opposite ladder)
     * if they overlaps, order volume will be subtracted from overlapped price volume.
     * That process repeats recursively until volume opposite orders with overlapped price will be exhausted.
     * If after that order's volume will still be more than zero,
     * it would be placed to corresponding ladder (bid/ask lists).
     * @param buyOrder - order from common storage which Operation type is BUY.
     */
    private void matchBuyOrder(Order buyOrder){
        Iterator<Order> iterator = ask.iterator();
        if(iterator.hasNext()){
            Order sellOrder = iterator.next();
            int sellPrice = sellOrder.getPrice();
            int sellVolume = sellOrder.getVolume();
            int buyPrice = buyOrder.getPrice();
            int buyVolume = buyOrder.getVolume();
            if(buyPrice >= sellPrice){
                if(buyVolume > sellVolume){
                    buyOrder.setVolume(buyVolume - sellVolume);
                    iterator.remove();
                    matchBuyOrder(buyOrder);
                }
                if(buyVolume == sellVolume){
                   iterator.remove();
                }
                if(buyVolume < sellVolume){
                    sellOrder.setVolume(sellVolume - buyVolume);
                }
            }
            if(buyPrice < sellPrice){
                bid.add(buyOrder);
            }
        } else {
            bid.add(buyOrder);
        }
    }

    /**
     * WARNING!!! That method must be invoked ONLY on bid list sorted at descending manner.
     * Checks if that order could be executed (order price overlaps the best price of opposite ladder)
     * if they overlaps, order volume will be subtracted from overlapped price volume.
     * That process repeats recursively until volume opposite orders with overlapped price will be exhausted.
     * If after that order's volume will still be more than zero,
     * it would be placed to corresponding ladder (bid/ask lists).
     * @param sellOrder - order from common storage which Operation type is SELL.
     */
    private void matchSellOrder(Order sellOrder){
        Iterator<Order> iterator = bid.iterator();
        if(iterator.hasNext()){
            Order buyOrder = iterator.next();
            int sellPrice = sellOrder.getPrice();
            int sellVolume = sellOrder.getVolume();
            int buyPrice = buyOrder.getPrice();
            int buyVolume = buyOrder.getVolume();
            if(sellPrice <= buyPrice){
                if(sellVolume > buyVolume){
                    sellOrder.setVolume(sellVolume - buyVolume);
                    iterator.remove();
                    matchSellOrder(sellOrder);
                }
                if(sellVolume == buyVolume){
                    iterator.remove();
                }
                if(sellVolume < buyVolume){
                    buyOrder.setVolume(buyVolume - sellVolume);
                }
            }
            if(sellPrice > buyPrice){
                ask.add(sellOrder);
            }
        } else {
            ask.add(sellOrder);
        }
    }
}
