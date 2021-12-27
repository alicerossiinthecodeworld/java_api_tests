import io.qameta.allure.Step;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class AcceptOrderTest {
    private OrderClient orderClient;
    private int status;
    private int orderId;
    private CourierClient courierClient;
    private CourierCredentials courierCredentials;
    private int courierId;
    private Order order;
    List<String> color;
    private int track;

    public AcceptOrderTest(List<String> color){
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
        courierClient = new CourierClient();
        orderClient = new OrderClient();
    }

    @Test
    public void courierCanAcceptOrderTest(){
        courierId = createCourierAndGetId();
        order = orderClient.getRandomOrder(color);
        orderId = createOrderAndGetOrderId(order);
        status = orderClient.acceptOrderReturnsStatus(orderId, courierId);
        courierClient.deleteCourier(courierId);
        assertEquals("Не принят заказ", 200, status);
    }

    //тест возвращает 404 без переданного id, что не соответствует спецификации, должен 400. Заигнорировала до исправления ситуации
    @Test
    public void canNotAcceptOrderIfNoIdIsPassed(){
        courierId = createCourierAndGetId();
        status = orderClient.acceptOrderWithNoOrderIdReturnsStatus(courierId);
            courierClient.deleteCourier(courierId);
        assertEquals("Принят заказ без id", 400, status);
    }

    //тест возвращает 404 без переданного id, что не соответствует спецификации, должен 400. Заигнорировала до исправления ситуации
    @Test
    public void canNotAcceptOrderIfNoCourierIsPassed(){
        order = orderClient.getRandomOrder(color);
        orderId = createOrderAndGetOrderId(order);
        status = orderClient.acceptOrderWithNoCourierIdReturnsStatus(orderId);
        assertEquals("Принят заказ без номера курьера", 400, status);
    }

    @Test
    public void canNotAcceptOrderIfWrongIdIsPassed(){
        courierId = createCourierAndGetId();
        order = orderClient.getRandomOrder(color);
        String wrongId = createOrderAndGetOrderId(order) + "order";
        courierClient.deleteCourier(courierId);
        status = orderClient.acceptOrderWithWrongOrderIdReturnsStatus(wrongId, courierId);
        assertNotEquals("Принят заказ с некорректным id", 200, status);
    }

    @Test
    public void canNotAcceptOrderIfWrongCourierIsPassed(){
        courierId = createCourierAndGetId();
        courierClient.deleteCourier(courierId);
        order = orderClient.getRandomOrder(color);
        orderId= createOrderAndGetOrderId(order);
        status = orderClient.acceptOrderReturnsStatus(orderId, courierId);
        assertEquals("Принят заказ c некорректным courierId", 404, status);
    }


    @Step("get order id by track")
    public int createOrderAndGetOrderId(Order someOrder){
        track = orderClient.createOrderReturnsResponse(order).getBody().path("track");
        return orderClient.getOrderIdByTrack(track);
    }

    @Step("create courier and get id")
    public int createCourierAndGetId(){
        Courier courier = Courier.randomize();
        courierClient.create(courier);

        courierCredentials = new CourierCredentials(courier.login, courier.password);
        return courierClient.login(CourierCredentials.getCourierCredentials(courier)).getBody().path("id");
    }
}
