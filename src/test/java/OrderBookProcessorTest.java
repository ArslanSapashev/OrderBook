import com.sapashev.Order;
import com.sapashev.OrderBookProcessor;
import com.sapashev.interfaces.Operation;
import com.sapashev.interfaces.OrderType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sony on 25.06.2016.
 */
public class OrderBookProcessorTest {
    @Test
    public void testMatchBuyOrder(){
        List<Order> orders = new ArrayList<Order>();
        orders.add(new Order("book-1", Operation.BUY,20,100,1, OrderType.ADD));
        orders.add(new Order("book-1", Operation.BUY,25,15,2, OrderType.ADD));
        orders.add(new Order("book-1", Operation.SELL,30,100,3, OrderType.ADD));

    }
}
