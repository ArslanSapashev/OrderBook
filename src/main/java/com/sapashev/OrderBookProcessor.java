package com.sapashev;

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

    public OrderBookProcessor (String bookName, Iterable<Order> orders) {
        this.book = bookName;
        this.orders = orders;
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
        Iterator<Order> iterator = orders.iterator();
        while (iterator.hasNext()){
            Order order = iterator.next();
            if (this.book.equals(order.getBook())){
                switch (order.getOperation()){
                    case BUY:
                        matchBuyOrder(order);
                        this.sort();    //СТОИТ ЛИ СОРТИТЬ КАЖДЫЙ РАЗ ???
                        break;
                    case SELL:
                        matchSellOrder(order);
                        this.sort();
                        break;
                }
            }
        }
    }

    /**
     * Sorts bid list in Descending manner and ask list in ascending manner.
     */
    private void sort(){
        Collections.sort(bid,new DescendingPrice());
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

    /**
     * Prints formatted order book to the standard output stream
     */
    private void print(){
        Order bidOrder = null;
        Order askOrder = null;
        List<String> listToPrint = new ArrayList<String>();
        printHeader();
        while (bid.iterator().hasNext() || ask.iterator().hasNext()){
            if(bid.iterator().hasNext()){
                bidOrder = bid.iterator().next();
            }
            if(ask.iterator().hasNext()){
                askOrder = ask.iterator().next();
            }
            joinBidAskPriceAndVolume(bidOrder, askOrder,listToPrint);
        }
        for(String s : listToPrint){
            System.out.println(s);
        }
    }

    /**
     * Prints header of order book
     */
    private void printHeader () {
        System.out.printf("Order book: %s\n", this.book);
        System.out.println("BID\t\tASK");
        System.out.println("Volume@Price\tVolume@Price");
    }

    /**
     * Joins volume and price of bid and ask orders to the one string and puts it to result string list.
     * @param bidOrder - bid order
     * @param askOrder - ask order
     * @param list - list that stores unified strings
     */
    private void joinBidAskPriceAndVolume (Order bidOrder, Order askOrder, List<String> list){
        String bidString;
        String askString;
        if(bidOrder != null){
            bidString = String.format("%d@%d",bidOrder.getVolume(),bidOrder.getPrice());
        } else {
            bidString = "---------";
        }
        if(askOrder != null){
            askString = String.format("%d@%d",askOrder.getVolume(),askOrder.getPrice());
        } else {
            askString = "---------";
        }
        list.add(bidString + askString);
    }

    public void run () {
        //TODO
    }
}
