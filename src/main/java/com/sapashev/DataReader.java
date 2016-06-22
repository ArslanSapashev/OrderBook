package com.sapashev;

import com.sapashev.interfaces.Addable;
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

    public DataReader(final Addable storage, final ReadConnector connector){
        this.storage = storage;
        this.connector = connector;
    }

    public void run () {
        boolean stop = false;
        Order order;
        while (!stop){
            if ((order = connector.read())!= null){
                storage.add(order);
            } else {
                stop = true;
            }
        }
    }
}
