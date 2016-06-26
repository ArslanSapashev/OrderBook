package com.sapashev;

import com.sapashev.interfaces.Addable;
import com.sapashev.interfaces.OrderType;
import com.sapashev.interfaces.ReadConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads data from Connector object and stores it to the storage object
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class DataReader implements Runnable{
    private final Addable storage;
    private final ReadConnector connector;  //source of data should know connector object
    private final DeleteMarker ordersToDelete;
    private final List<String> books;
    private final Logger LOG = LoggerFactory.getLogger(DataReader.class);

    public DataReader(final Addable storage, final ReadConnector connector, final DeleteMarker ordersToDelete, List<String> books){
        this.storage = storage;
        this.connector = connector;
        this.ordersToDelete = ordersToDelete;
        this.books = books;
    }

    public void run () {
        boolean stop = false;
        Order order;
        LOG.debug(String.format("Reading begin at %d%n", System.currentTimeMillis()));
        try {
            connector.parse();
        } catch (FileNotFoundException e) {
            LOG.error("FileNotFoundException " + e);
        }
        while (!stop){
            order = connector.read();
            if(order == null){
                stop = true;
                LOG.debug(String.format("Reading ended at %d%n", System.currentTimeMillis()));
                break;
            }
            if(!books.contains(order.getBook())){
                books.add(order.getBook());
            }
            if(order.getType() == OrderType.DELETE){
                synchronized (ordersToDelete){
                    ordersToDelete.keys.add(order.getOrderID());
                }
                continue;
            }
            if (order.getType() == OrderType.ADD){
                storage.add(order);
            }
        }
    }
}
