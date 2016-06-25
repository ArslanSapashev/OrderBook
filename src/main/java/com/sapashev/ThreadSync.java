package com.sapashev;

/**
 * Stores thread synchronization info.
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class ThreadSync {
    public volatile boolean isReadingFinished;
    public volatile boolean isDeletingFinished;
}
