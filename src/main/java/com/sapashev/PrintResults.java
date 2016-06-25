package com.sapashev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sony on 25.06.2016.
 */
public class PrintResults {
    private final String book;
    private final List<Order> bid;
    private final List<Order> ask;
    private final Logger LOG = LoggerFactory.getLogger(PrintResults.class);


    public PrintResults (Result result) {
        if(result==null){
            LOG.error("result equals " + result + Thread.currentThread());
        }
        this.book = result.getBook();
        this.bid = result.getBid();
        this.ask = result.getAsk();
    }

    /**
     * Prints formatted order book to the standard output stream
     */
    public void print(){
        LOG.debug(String.format("OrderBookProcessor begins at %d%n", System.currentTimeMillis()));
        List<String> listToPrint = iterateBidAskForComposite();
        printHeader();
        for(String s : listToPrint){
            System.out.println(s);
        }
        LOG.debug(String.format("OrderBookProcessor ended at %d%n", System.currentTimeMillis()));
    }

    /**
     * Iterates through bid and ask order lists to join price and volume in one string.
     * @return
     */
    private List<String> iterateBidAskForComposite () {
        Order bidOrder = null;
        Order askOrder = null;
        Iterator<Order> iterBID = bid.iterator();
        Iterator<Order> iterASK = ask.iterator();

        List<String> listToPrint = new ArrayList<String>();
        while (iterBID.hasNext() || iterASK.hasNext()){
            if(iterBID.hasNext()){
                bidOrder = iterBID.next();
            }
            if(iterASK.hasNext()){
                askOrder = iterASK.next();
            }
            joinPriceAndVolume(bidOrder, askOrder,listToPrint);
            bidOrder = null;
            askOrder = null;
        }
        return listToPrint;
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
    private void joinPriceAndVolume (Order bidOrder, Order askOrder, List<String> list){
        String bidString;
        String askString;
        if(bidOrder != null){
            bidString = String.format("%d@%.2f\t",bidOrder.getVolume(),(float)(bidOrder.getPrice())/100);
        } else {
            bidString = "---------";
        }
        if(askOrder != null){
            askString = String.format("%d@%.2f",askOrder.getVolume(),(float)(askOrder.getPrice())/100);
        } else {
            askString = "---------";
        }
        list.add(bidString + askString);
    }
}
