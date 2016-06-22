package com.sapashev.interfaces;

/**
 * Declares method to get order from storage (could wrap any kind of lists, sets, maps, DB).
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public interface Getable <E, K> {
    E get(K key);
}
