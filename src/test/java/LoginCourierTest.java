import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginCourierTest {
    private CourierClient courierClient;
    private int courierId;
    private int status;
    private Courier courier;

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
    public void successfulLoginReturnsId(){
        courier = createCourier();
        courierId = getCourierId(courier);
        status = loginGetStatus(courier);
        assertEquals("Неуспешный логин",200, status);
        assertNotEquals(0, courierId);
    }
    @Test
    public void courierCanLogIn(){
        courier = createCourier();
        courierId = getCourierId(courier);
        status = loginGetStatus(courier);
        assertEquals("Провалился логин", 200, status);
    }
    @Test
    public void courierCantLogInWithoutLogin(){
        courier = createCourier();
        courierId = getCourierId(courier);
        status = courierClient.loginWithoutLoginReturnStatus(CourierCredentials.getCourierCredentials(courier));
        assertEquals("Можно войти без логина",400, status);
    }


    @Test
    public void courierCantLogInWithoutPassword(){
        courier = createCourier();
        courierId = getCourierId(courier);
        status = courierClient.loginWithoutPasswordReturnStatus(CourierCredentials.getCourierCredentials(courier));
        assertEquals("Можно войти без логина",400, status);
    }

    @Test
    public void courierCantLogInWithWrongPassword(){
        courier = createCourier();
        String newPassword = courier.randomizePassword();
        String login = CourierCredentials.getCourierCredentials(courier).login;
        courierId = getCourierId(courier);
        status = courierClient.loginWithStringLogopassReturnStatus(login, newPassword);
        assertEquals("Нельзя войти с неправильным паролем",404, status);
    }


    @Test
    public void courierCantLogInWithWrongLogin() {
        courier = createCourier();
        String password = CourierCredentials.getCourierCredentials(courier).password;
        String login = courier.randomizeLogin();
        courierId = getCourierId(courier);
        status = courierClient.loginWithStringLogopassReturnStatus(login, password);
        assertEquals("Нельзя войти с неправильным логином", 404, status);
    }

    @Step("CreateCourier")
    public Courier createCourier() {
        Courier courier = Courier.randomize();
        courierClient.create(courier);

        return courier;
    }


    @Step("try to login to get status")
    public int loginGetStatus(Courier courier){
        return courierClient.login(CourierCredentials.getCourierCredentials(courier)).getStatusCode();
    }

    @Step("Login to Get Id")
    public int getCourierId(Courier courier) {
        return courierClient.login(CourierCredentials.getCourierCredentials(courier)).getBody().path("id");
    }
}
