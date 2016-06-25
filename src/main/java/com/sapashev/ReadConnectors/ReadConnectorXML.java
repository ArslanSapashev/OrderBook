package com.sapashev.ReadConnectors;

import com.sapashev.Order;
import com.sapashev.interfaces.ReadConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads quotes from XML file and wraps each entry in Order object.
 * @author Arslan Sapashev
 * @since 21.06.2016
 * @version 1.0
 */
public class ReadConnectorXML implements ReadConnector {
    private final String source;
    List<Order> orders = new ArrayList<Order>();
    int index = 0;
    boolean isParsed = false;
    private final Logger LOG = LoggerFactory.getLogger(ReadConnector.class);

    public ReadConnectorXML (String source) {
        this.source = source;
    }

    public Order read () {
        if(!isParsed){
            parse();
            isParsed = true;
        }
        Order order = null;
        if(index < orders.size()){
            order = orders.get(this.index++);
        }
        return order;
    }

    private void parse(){
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXHandler handler = new SAXHandler(orders);

        try {
            SAXParser parser = parserFactory.newSAXParser();
            parser.parse(source,handler);
        } catch (ParserConfigurationException e) {
            LOG.error("ParseConfigurationException " + e);
        } catch (SAXException e) {
            LOG.error("SAXException " + e);
        } catch (IOException e){
            LOG.error("IOException" + e);
        }
    }
}
