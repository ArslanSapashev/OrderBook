package com.sapashev;

import com.sapashev.ReadConnectors.ReadConnectorXML;
import com.sapashev.Storages.StorageLinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Conducts order book processing.
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class SimpleProcessor {
    private final Logger LOG = LoggerFactory.getLogger(SimpleProcessor.class);

    public static void main (String[] args) {
        new SimpleProcessor().start(args[0]);
    }

    public void start(String source){

        ThreadSync sync = new ThreadSync();
        StorageLinkedHashMap storage = new StorageLinkedHashMap();
        DeleteMarker deleteMarker = new DeleteMarker();
        List<Thread> threads = new ArrayList<Thread>();
        List<String> books = new ArrayList<String>();
        List<Result> results = Collections.synchronizedList(new ArrayList<Result>());

        ReadConnectorXML readConnectorXML = new ReadConnectorXML(source);
        DeleteThread deleteThread = new DeleteThread(storage,deleteMarker,sync);
        DataReader dataReader = new DataReader(storage,readConnectorXML,deleteMarker,sync, books);

        Thread thread1 = new Thread(deleteThread);
        Thread thread2 = new Thread(dataReader);
        thread1.start();
        thread2.start();
        LOG.debug("DataReader and DeleteThread has beeing started");
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            LOG.error("InterruptedException" + e);
        }
        for(String book : books){
            threads.add(new Thread(new OrderBookProcessor(book,storage, results)));
        }
        for(Thread t : threads){
            t.start();
        }
        for(Thread t : threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(Result r : results){
            new PrintResults(r).print();
        }
    }
}
