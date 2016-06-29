package com.sapashev.readconnectors;

import com.sapashev.Order;
import com.sapashev.interfaces.Operation;
import com.sapashev.interfaces.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads quotes from XML file and wraps each entry in Order object.
 * @author Arslan Sapashev
 * @since 26.06.2016
 * @version 1.0
 */
public class SimpleParser2 {
    List<Order> orders;
    String source;
    private final Logger LOG = LoggerFactory.getLogger(SimpleParser.class);

    public SimpleParser2 (List<Order> orders, String source) {
        this.orders = orders;
        this.source = source;
    }

    public void parse () throws FileNotFoundException,IOException {
        BufferedReader reader = new BufferedReader(new FileReader(source));
        String line;
        LOG.debug(String.format("Parse started at %s", System.currentTimeMillis()));
        while ((line = reader.readLine()) != null){
            if(line.startsWith("<A")){
                List<String> list = new ArrayList<>();
                readText(line,line.indexOf('"'),list);
                String book = list.get(0);
                Operation operation = (list.get(1).startsWith("B")) ? Operation.BUY : Operation.SELL;
                int price = (int)(Double.valueOf(list.get(2))*100);
                int volume = Integer.parseInt(list.get(3));
                int orderID = Integer.parseInt(list.get(4));
                OrderType type = OrderType.ADD;
                orders.add(new Order(book,operation,price,volume,orderID,type));
            }
            if(line.startsWith("<D")){
                List<String> list = new ArrayList<>();
                readDeleteText(line,line.indexOf('"'),list);
                String book = list.get(0);
                OrderType type = OrderType.DELETE;
                int orderID = Integer.parseInt(list.get(1));
                orders.add(new Order(book,Operation.BUY,0,0,orderID,type));
            }
        }
        LOG.debug(String.format("Parse finished at %s", System.currentTimeMillis()));
    }

    private void readText(String text, int index, List<String> list){
        int nextIndex = text.indexOf('"',index+1);
        list.add(text.substring(index+1,nextIndex).trim());
        if(list.size() < 5){
            readText(text,text.indexOf('"',nextIndex+1),list);
        }
        return;
    }

    private void readDeleteText(String text, int index, List<String> list){
        int nextIndex = text.indexOf('"',index+1);
        list.add(text.substring(index+1,nextIndex).trim());
        if(list.size() < 2){
            readDeleteText(text,text.indexOf('"',nextIndex+1),list);
        }
        return;
    }
}
