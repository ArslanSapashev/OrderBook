package com.sapashev.interfaces;

import com.sapashev.Order;

import java.io.FileNotFoundException;

/**
 * Declares method for reading(retrieving) data from external source (like files, sockets etc.)
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public interface ReadConnector {
    Order read();
    void parse() throws FileNotFoundException;
}
