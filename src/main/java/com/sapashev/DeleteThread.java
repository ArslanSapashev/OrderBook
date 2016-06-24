package com.sapashev;

import com.sapashev.interfaces.Removable;

import java.util.Iterator;

/**
 * Deletes orders (cancelled by a user).
 * Due to performance optimizations
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class DeleteThread implements Runnable{
    private final DeleteMarker ordersToDelete;
    private final Removable storage;

    public DeleteThread(final Removable storage, DeleteMarker deleteMarker){
        this.storage = storage;
        this.ordersToDelete = deleteMarker;
    }

    //Данный поток удаляет приказы по их orderID, т.к. в момент получения уведомления от другого потока (notify)
    //данный поток может пропусть, то DataReader помещает Order на удаление в list объекта DeleteMarker.
    //а данный объект при просыпании будет все удалять все объекты в списке DeleteMarker, после этого данный список
    // обнуляется тем самым гарантируется что ордера на удаления которые уже были обработаны не будут повторно выполнены.
    public void run () {
        boolean stop = false;
        while (!stop){
            try {
                synchronized (ordersToDelete){
                    Iterator<Integer> iter = ordersToDelete.iterator();
                    while (iter.hasNext()){
                        storage.remove(iter.next());
                    }
                    ordersToDelete.keys.clear();
                    ordersToDelete.wait();
                }
            }
            catch (InterruptedException ex){
                //TODO log here
            }
        }
    }
}
