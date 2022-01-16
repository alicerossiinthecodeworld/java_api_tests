import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetOrdersTest {

    private OrderClient orderClient;
    private int status;
    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    public void getOrderWithLimitAndPageReturnsSuccessTest(){
         Response response = orderClient.getOrdersReturnsResponse();
         status = response.getStatusCode();
         assertEquals(200, status);
    }
    @Test
    public void getOrdersReturnsOrdersTest(){
        assertNotNull(orderClient.getOrdersReturnsOrders());
    }

    @Test
    public void getOrderWithPageAndLimitReturnsCorrectNumberOfOrdersTest(){
        Response response = orderClient.getOrdersWithLimitAndPageReturnsResponse(10,1);
        List<String> names = response.getBody().path("orders.firstName");
        int quantity = names.size();
        status = response.getStatusCode();
        assertEquals("вывелось неверное число заказов",10,quantity);
        assertEquals("некорректный статус",200,status);
    }
}

