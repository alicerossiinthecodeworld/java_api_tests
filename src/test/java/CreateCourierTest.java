import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class CreateCourierTest {
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void teardown() {
        if (courierId > 0) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    public void courierCreatedSuccessTest() {
        Courier courier = Courier.randomize();
        boolean isCreated = courierClient.createReturnsIfOk(courier);
        courierId = getCourierId(courier);
        assertTrue("курьер не создан", isCreated);
        assertThat("Не получен id курьера", courierId, is(not(0)));
    }

    @Test
    public void impossibleToCreateTwoSameCouriers() {
        Courier courier = Courier.randomize();
        createCourier(courier);
        courierId = getCourierId(courier);
        int status = createCourier(courier).getStatusCode();
        assertEquals(409, status);
    }

    @Test
    public void impossibleToCreateTwoCouriersWithTheSameLogin() {
        Courier courier = Courier.randomize();
        Courier courier1 = new Courier(courier.login, "ururu", "Василий");
        courierClient.create(courier);
        courierId = getCourierId(courier);
        int status = createCourier(courier1).getStatusCode();
        assertEquals(409, status);
    }

    @Test
    public void impossibleToCreateCourierWithoutLogin() {
        Courier courier = Courier.randomize();
        int status = createCourier(courier).getStatusCode();
        assertEquals("Пришёл некорректный статус", 400, status);
    }

    @Test
    public void impossibleToCreateCourierWithoutPassword() {
        Courier courier = Courier.randomize();
        int status = courierClient.createWithoutPasswordReturnsStatus(courier);
        assertEquals("Можно создать курьера без пароля", 400, status);
    }

    @Test
    public void possibleToCreateCourierWithoutFirstName() {
        Courier courier = Courier.randomize();
        int status = courierClient.createWithoutFirstNameReturnsStatus(courier);
        assertEquals("Можно создать курьера без имени", 201, status);
    }


    @Step("CreateCourier")
    public Response createCourier(Courier courier) {
        courierClient.create(courier);
        return courierClient.create(courier);
    }

    @Step("Login to Get Id")
    public int getCourierId(Courier courier) {
        return courierClient.login(CourierCredentials.getCourierCredentials(courier)).getBody().path("id");
    }
}
