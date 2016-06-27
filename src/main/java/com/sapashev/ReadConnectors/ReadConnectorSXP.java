package com.sapashev.ReadConnectors;

import com.sapashev.Order;
import com.sapashev.interfaces.ReadConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Wraps simple parser. "orders" list contains raw (not sorted and Delete orders inclusive) orders from XML file.
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class ReadConnectorSXP implements ReadConnector {
    private final String source;
    List<Order> orders = new ArrayList<Order>();
    int index = 0;
    private final Logger LOG = LoggerFactory.getLogger(ReadConnector.class);

    public ReadConnectorSXP (String source) {
        this.source = source;
    }

    /**
     * Returns to the DataRead object, next element of list of orders, that has been read by SAXHandler.
     * @return - next order.
     */
    public Order read () {
        Order order = null;
        if(index < orders.size()){
            order = orders.get(this.index++);
        }
        return order;
    }

    public void parse() throws FileNotFoundException {
        try {
            new SimpleParser2(orders,source).parse2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
