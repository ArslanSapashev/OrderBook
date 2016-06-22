package com.sapashev.ReadConnectors;

import com.sapashev.Order;
import com.sapashev.interfaces.ReadConnector;

/**
 * Stores trade orders
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class ReadConnectorXML implements ReadConnector {
    private final String source;

    public ReadConnectorXML (String source) {
        this.source = source;
    }

    public Order read () {
        //TODO
        return null;
    }
}
