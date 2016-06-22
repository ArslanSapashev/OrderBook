package com.sapashev;

import com.sapashev.interfaces.Addable;
import com.sapashev.interfaces.OrderType;
import com.sapashev.interfaces.ReadConnector;

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

    public DataReader(final Addable storage, final ReadConnector connector, DeleteMarker ordersToDelete){
        this.storage = storage;
        this.connector = connector;
        this.ordersToDelete = ordersToDelete;
    }

    public void run () {
        boolean stop = false;
        Order order;
        while (!stop){
            order = connector.read();
            if(order.getType() == OrderType.DELETE){
                ordersToDelete.keys.add(order.getOrderID());
                synchronized (ordersToDelete){
                    ordersToDelete.notify();
                }
            }
            if (order != null){
                storage.add(order);
            } else {
                stop = true;
            }
        }
    }
}
