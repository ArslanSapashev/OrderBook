package com.sapashev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Prints books. Data will be printed in columns in compacted form.
 * @author Arslan Sapashev
 * @since 26.06.2016
 * @version 1.0
 */
public class PrintResults {
    private final String book;
    private final List<Order> bid;
    private final List<Order> ask;
    private List<Map.Entry<Integer, Integer>> compactedBID;
    private List<Map.Entry<Integer, Integer>> compactedASK;
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

        /**
         * Converts compacted Map<Integer, Integer> to the list and sorts it in descending order.
         */
        Map<Integer, Integer> map = compaction(bid);
        compactedBID = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
        Collections.sort(compactedBID, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare (Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                int result = 0;
                if(o1.getKey() > o2.getKey()){
                    result = -1;
                } else if(o1.getKey() < o2.getKey()){
                    result = 1;
                } else if(o1.getKey() == o2.getKey()){
                    result = 0;
                }
                return result;
            }
        });

        /**
         * Converts compacted Map<Integer, Integer> to the list and sorts it in ascending order.
         */
        map = compaction(ask);
        compactedASK = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
        Collections.sort(compactedASK, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare (Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                int result = 0;
                if(o1.getKey() > o2.getKey()){
                    result = 1;
                } else if(o1.getKey() < o2.getKey()){
                    result = -1;
                } else if(o1.getKey() == o2.getKey()){
                    result = 0;
                }
                return result;
            }
        });

        List<String> listToPrint = iterateBidAskForComposite();
        printHeader();
        for(String s : listToPrint){
            System.out.println(s);
        }
        LOG.debug(String.format("OrderBookProcessor ended at %d%n", System.currentTimeMillis()));
    }

    /**
     * Iterates through bid and ask compacted lists to join price and volume in one string.
     * @return summarized list, ready to print.
     */
    private List<String> iterateBidAskForComposite () {
        Map.Entry<Integer, Integer> bidOrder = null;
        Map.Entry<Integer, Integer> askOrder = null;
        Iterator<Map.Entry<Integer, Integer>> iterBID = compactedBID.iterator();
        Iterator<Map.Entry<Integer, Integer>> iterASK = compactedASK.iterator();

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
     * Joins volume and price of bid and ask ladders to the one string and puts it to the resulting string list.
     * @param bidOrder - bid order
     * @param askOrder - ask order
     * @param list - list that stores unified strings
     */
    private void joinPriceAndVolume (Map.Entry<Integer, Integer> bidOrder, Map.Entry<Integer, Integer> askOrder, List<String> list){
        String bidString;
        String askString;
        if(bidOrder != null){
            bidString = String.format("%d@%.2f\t",bidOrder.getValue(),(float)(bidOrder.getKey())/100);
        } else {
            bidString = "---------";
        }
        if(askOrder != null){
            askString = String.format("%d@%.2f",askOrder.getValue(),(float)(askOrder.getKey())/100);
        } else {
            askString = "---------";
        }
        list.add(bidString + askString);
    }

    /**
     * Compacts bid and ask ladders by summarizing volumes of equals price levels.
     * Returned as Map <Integer, Integer>.
     * @param source
     * @return
     */
    private Map<Integer, Integer> compaction(List<Order> source) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (Order o : source) {
            if (map.containsKey(o.getPrice())) {
                Integer i = map.get(o.getPrice());
                map.put(o.getPrice(), i + o.getVolume());
            } else {
                map.put(o.getPrice(), o.getVolume());
            }
        }
        return map;
    }
}
