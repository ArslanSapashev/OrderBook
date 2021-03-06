package com.sapashev.readconnectors;

import com.sapashev.Order;
import com.sapashev.interfaces.Operation;
import com.sapashev.interfaces.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * Reads quotes from XML file and wraps each entry in Order object.
 * @author Arslan Sapashev
 * @since 24.06.2016
 * @version 1.0
 */
public class SAXHandler extends DefaultHandler {
    List<Order> orders;
    private final Logger LOG = LoggerFactory.getLogger(SAXHandler.class);

    public SAXHandler (List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public void startDocument () throws SAXException {
        LOG.debug(String.format("XML parsing started at %d%n",System.currentTimeMillis()));
    }

    @Override
    public void endDocument () throws SAXException {
        orders.add(null); //этот null вываливается в StorageList строка 26
        LOG.debug(String.format("XML file parsing finished at %d%n", System.currentTimeMillis()));
    }

    /**
     * Reads each element, creates Order object and places it to list.
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement (String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if("AddOrder".equalsIgnoreCase(qName)){
            OrderType orderType = OrderType.ADD;
            String book = attributes.getValue("book");
            Operation operation = attributes.getValue("operation").equals("BUY") ? Operation.BUY : Operation.SELL;
            int price = (int)(Double.valueOf(attributes.getValue("price"))* 100);
            int volume = Integer.parseInt(attributes.getValue("volume"));
            int orderId = Integer.parseInt(attributes.getValue("orderId"));
            orders.add(new Order(book,operation,price,volume,orderId,orderType));
        }
        if("DeleteOrder".equalsIgnoreCase(qName)){
            OrderType orderType = OrderType.DELETE;
            String book = attributes.getValue("book");
            Operation operation = null;
            int price = 0;
            int volume = 0;
            int orderId = Integer.parseInt(attributes.getValue("orderId"));
            orders.add(new Order(book,operation,price,volume,orderId,orderType));
        }
    }
}
