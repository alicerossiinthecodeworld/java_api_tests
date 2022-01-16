import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class GetOrderByTrackTest {
    private OrderClient orderClient;
    private Order order;
    List<String> color;
    private int track;

    public GetOrderByTrackTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {List.of("BLACK", "GREY"),},
                {List.of("BLACK"),},
                {List.of("GREY")},
                {List.of("")}
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    public void canGetOrderByTrackTest(){
        order = orderClient.getRandomOrder(color);
        track = orderClient.createOrderReturnsResponse(order).getBody().path("track");
        String name = orderClient.getOrderInfoByTrackReturnsResponse(track).getBody().path("order.firstName");
        assertNotNull("не возвращаются данные заказа",name);
    }

    @Test
    public void canNotGetOrderByTrackTestWithoutTrackTest(){
        int status = orderClient.getOrderInfoWithoutTrackReturnsStatus();
        assertEquals("некорректный статус",400, status);
    }
    @Test
    public void canNotGetOrderByWrongTrackTest(){
        order = orderClient.getRandomOrder(color);
        track = orderClient.createOrderReturnsResponse(order).getBody().path("track") ;
        int status = orderClient.getOrderInfoByTrackReturnsResponse(track+100).getStatusCode();
        assertEquals("некорректный статус",404, status);
    }
}
