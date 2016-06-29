import com.sapashev.SimpleProcessor;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;

/**
 * Test how much time it takes to process test xml file. Elapsed time should be up to 6 seconds.
 * Created by Sony on 29.06.2016.
 */
public class OrderBookTest {
    @Test(timeout = 6000)
    public void test(){
        SimpleProcessor processor = new SimpleProcessor();
        processor.start("c:\\workspace\\orders.xml");
    }
}
