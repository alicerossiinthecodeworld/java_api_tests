import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    List<String> color;

    public CreateOrderTest(List<String> color){
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

    private OrderClient orderClient;
    private int track;
    private int status;
    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    public void createOrderTest(){
        Response response = createOrder(color);
        status =  response.getStatusCode();
        track = getTrack(response);
        assertEquals("Неуспешное создание курьера", 201, status);
    }

    @Test
    public void cancelOrderSuccess(){
        Response response = createOrder(color);
        track = getTrack(response);
        status = orderClient.cancelOrderReturnsStatus(track);
        assertEquals("не отменяется заказ",200,status);
    }
    @Step("Create order get status")
    public Response createOrder(List<String> color){
        Order order = orderClient.getRandomOrder(color);
        return orderClient.createOrderReturnsResponse(order);
    }

    @Step ("get track")
    public int getTrack(Response response){
        return response.getBody().path("track");
    }
}
