import io.qameta.allure.Step;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeleteCourierTest {
    private CourierClient courierClient;
    private int courierId;
    private int status;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    public void canSuccessfullyDeleteACourier() {
        courier = createCourier();
        courierId = getCourierId(courier);
        status = deleteGetStatus(courierId);
        assertEquals("Неуспешное удаление", 200, status);
    }

    @Test
    public void loginFailsAfterDelete() {
        courier = createCourier();
        courierId = getCourierId(courier);
        deleteGetStatus(courierId);
        status = loginGetStatus(courier);
        assertEquals("Удалось залогиниться в удалённого курьера", 404, status);
    }

    @Test
    public void deleteFailsIfNoIdIsSent() {
        status = courierClient.deleteWithoutIdReturnsStatus();
        assertEquals("Успешный delete запрос без id", 404, status);
    }

    @Test
    public void deleteFailsIfNotExistingIdIsSent() {
        courier = createCourier();
        courierId = getCourierId(courier);
        deleteGetStatus(courierId);
        status = deleteGetStatus(courierId);
        assertEquals("Успешный delete запрос на несуществующий id", 404, status);
    }

    @Step("CreateCourier")
    public Courier createCourier() {
        Courier courier = Courier.randomize();
        courierClient.create(courier);

        return courier;
    }

    @Step("Login to Get Id")
    public int getCourierId(Courier courier) {
        return courierClient.login(CourierCredentials.getCourierCredentials(courier)).getBody().path("id");
    }

    @Step("delete Courier")
    public int deleteGetStatus(int courierId){
       return courierClient.deleteCourier(courierId).getStatusCode();
    }

    @Step("try to login to get status")
    public int loginGetStatus(Courier courier){
        return courierClient.login(CourierCredentials.getCourierCredentials(courier)).getStatusCode();
    }
}
