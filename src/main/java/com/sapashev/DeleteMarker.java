package com.sapashev;

/**
 * Contains information (key) to determine which order to delete.
 * Used by DataReader as object to store reference to Order object that must be deleted and
 * synchronization object through which DeleteThread notified to delete certain Order object.
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class DeleteMarker {
    public int key;
}
