package com.sapashev;

import com.sapashev.interfaces.Addable;
import com.sapashev.interfaces.OrderType;
import com.sapashev.interfaces.ReadConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final List<Thread> threads;
    private final List<String> books = new ArrayList<String>();
    private final ThreadSync sync;
    private final Logger LOG = LoggerFactory.getLogger(DataReader.class);

    public DataReader(final Addable storage, final ReadConnector connector, final DeleteMarker ordersToDelete, final List<Thread> threads, final ThreadSync sync){
        this.storage = storage;
        this.connector = connector;
        this.ordersToDelete = ordersToDelete;
        this.threads = threads;
        this.sync = sync;
    }

    public void run () {
        boolean stop = false;
        Order order;
        while (!stop){
            order = connector.read();
            if(order == null){
                stop = true;
                this.sync.isReadingFinished = true;
                LOG.debug("Reading ended");
                synchronized (ordersToDelete){
                    ordersToDelete.notify();
                }
                break;
            }
            if(!books.contains(order.getBook())){
                books.add(order.getBook());
                threads.add(new Thread(new OrderBookProcessor(order.getBook(),(Iterable<Order>) storage)));
                LOG.debug("New processing thread added");
            }
            if(order.getType() == OrderType.DELETE){
                synchronized (ordersToDelete){
                    ordersToDelete.keys.add(order.getOrderID());
                    ordersToDelete.notify();
                }
                LOG.debug("DeleteThread invoked");
                continue;
            }
            if (order.getType() == OrderType.ADD){
                storage.add(order);
            }
        }
    }
}
