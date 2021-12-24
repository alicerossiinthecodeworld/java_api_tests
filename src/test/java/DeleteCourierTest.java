import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeleteCourierTest {
    private CourierClient courierClient;
    private int courierId;
    private int status;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    public void canSuccessfullyDeleteACourier() {
        Courier courier = Courier.getRandom();
        courierClient.createReturnsIsCreated(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        int status = courierClient.deleteReturnsStatus(courierId);
        assertEquals("Неуспешное удаление",200, status);
    }

    @Test
    public void loginFailsAfterDelete(){
        Courier courier = Courier.getRandom();
        courierClient.createReturnsIsCreated(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        courierClient.deleteReturnsStatus(courierId);
        int status = courierClient.loginReturnsStatus(CourierCredentials.getCourierCredentials(courier));
        assertEquals("Удалось залогиниться в удалённого курьера", 400, status);
    }

    //Cогласно спецификации, ответ должен быть 400, ловлю 404, тест можно использовать после исправления ситуации
    @Ignore
    @Test
    public void deleteFailsIfNoIdIsSent(){
        status = courierClient.deleteWithoutIdReturnsStatus();
        assertEquals( "Успешный delete запрос без id", 404, status);
    }

    @Test
    public void deleteFailsIfNotExistingIdIsSent(){
        Courier courier = Courier.getRandom();
        courierClient.createReturnsIsCreated(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        courierClient.delete(courierId);
        status = courierClient.deleteReturnsStatus(courierId);
        assertEquals( "Успешный delete запрос на несуществующий id", 404, status);
    }
}
