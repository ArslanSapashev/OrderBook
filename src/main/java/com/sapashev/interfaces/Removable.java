package com.sapashev.interfaces;

import com.sapashev.Order;

/**
 * Declares methods to remove order from the storage (could wrap any kind of lists, sets, maps).
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public interface Removable <T> {
    boolean remove(T element);
}
