package com.sapashev;

import com.sapashev.interfaces.Removable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * Deletes orders (cancelled by a user).
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class DeleteThread implements Runnable{
    private final DeleteMarker ordersToDelete;
    private final Removable storage;
    private final Thread thread;
    private final Logger LOG = LoggerFactory.getLogger(DeleteThread.class);

    public DeleteThread(final Removable storage, final DeleteMarker deleteMarker, final Thread thread){
        this.storage = storage;
        this.ordersToDelete = deleteMarker;
        this.thread = thread;
    }

    /**
     * That thread processes delete-orders based on orderID. The orderID is key to delete orders.
     * When DataReader encounters in list of orders DeleteOrder it doesn't place it to list of orders, instead it
     * places it to the list orderToDelete and notifies the thread that listens orderToDelete object.
     * Due to DeleteThread could miss notification, all delete-orders are placed in the list, which will be processed
     * one-by-one by DeleteThread. That garantees that all delete-orders will be processed.
     */
    public void run () {
        try {
            thread.join();
        } catch (InterruptedException e) {
            LOG.error("Interrupted exception ", e);
        }
        Iterator<Integer> iter = ordersToDelete.iterator();
        while(iter.hasNext()){
            storage.remove(iter.next());
        }
    }
}
