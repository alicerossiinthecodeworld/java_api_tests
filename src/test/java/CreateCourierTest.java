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
            courierClient.delete(courierId);
        }
    }

    @Test
    public void courierCreated() {
        Courier courier = Courier.getRandom();
        boolean isCreated = courierClient.createReturnsIsCreated(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        assertTrue("курьер не создан", isCreated);
        assertThat("Не получен id курьера", courierId, is(not(0)));
    }

    @Test
    public void impossibleToCreateTwoSameCouriers() {
        Courier courier = Courier.getRandom();
        courierClient.createReturnsIsCreated(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        int status = courierClient.createReturnsStatus(courier);
        assertEquals(409, status);
    }

    @Test
    public void impossibleToCreateTwoCouriersWithTheSameLogin() {
        Courier courier = Courier.getRandom();
        Courier courier1 = new Courier(courier.login, "ururu", "Василий");
        courierClient.createReturnsIsCreated(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        int status = courierClient.createReturnsStatus(courier1);
        assertEquals(409, status);
    }

    @Test
    public void impossibleToCreateCourierWithoutLogin() {
        Courier courier = Courier.getRandom();
        int status = courierClient.createWithoutLoginReturnsStatus(courier);
        assertEquals("Курьер создан без логина", 400, status);
    }

    @Test
    public void createdCourierReturnsSuccess() {
        Courier courier = Courier.getRandom();
        int status = courierClient.createReturnsStatus(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        assertEquals("Создание курьера не возвращает 201", 201, status);
    }

    @Test
    public void impossibleToCreateCourierWithoutPassword() {
        Courier courier = Courier.getRandom();
        int status = courierClient.createWithoutPasswordReturnsStatus(courier);
        assertEquals("Можно создать курьера без пароля", 400, status);
    }

    @Test
    public void possibleToCreateCourierWithoutFirstName() {
        Courier courier = Courier.getRandom();
        int status = courierClient.createWithoutFirstNameReturnsStatus(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        assertEquals("Нельзя создать курьера без имени", 201, status);
    }
}
