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
    private final ThreadSync sync;

    public DeleteThread(final Removable storage, final DeleteMarker deleteMarker, final ThreadSync sync){
        this.storage = storage;
        this.ordersToDelete = deleteMarker;
        this.sync = sync;
    }

    //Данный поток удаляет приказы по их orderID, т.к. в момент получения уведомления от другого потока (notify)
    //данный поток может пропусть, то DataReader помещает Order на удаление в list объекта DeleteMarker.
    //а данный объект при просыпании будет все удалять все объекты в списке DeleteMarker, после этого данный список
    // обнуляется тем самым гарантируется что ордера на удаления которые уже были обработаны не будут повторно выполнены.
    public void run () {
        do {
            try {
                synchronized (ordersToDelete){
                    Iterator<Integer> iter = ordersToDelete.iterator();
                    while (iter.hasNext()){
                        storage.remove(iter.next());
                    }
                    ordersToDelete.keys.clear();
                    if(sync.isReadingFinished){
                        sync.isDeletingFinished = true;
                        break;
                    }
                    ordersToDelete.wait();
                }
            }
            catch (InterruptedException ex){
                //TODO log here
            }
        }while (!sync.isReadingFinished);
    }
}
