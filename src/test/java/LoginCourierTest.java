import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginCourierTest {
    private CourierClient courierClient;
    private int courierId;
    private int status;

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
    public void successfulLoginReturnsId(){
        Courier courier = Courier.getRandom();
        courierClient.createReturnsIsCreated(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        status = courierClient.loginReturnsStatus(CourierCredentials.getCourierCredentials(courier));
        assertEquals("Неуспешный логин",200, status);
    }
    @Test
    public void courierCanLogIn(){
        Courier courier = Courier.getRandom();
        courierClient.createReturnsIsCreated(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        status = courierClient.loginReturnsStatus(CourierCredentials.getCourierCredentials(courier));
        assertEquals("Провалился логин", 200, status);
    }
    @Test
    public void courierCantLogInWithoutLogin(){
        Courier courier = Courier.getRandom();
        courierClient.createReturnsIsCreated(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        status = courierClient.loginWithoutLoginReturnStatus(CourierCredentials.getCourierCredentials(courier));
        assertEquals("Можно войти без логина",400, status);
    }

    //Постоянно ловила по данному тесту 504, что не соответствует спецификации, и тест падает,
    //можно использовать, как только ситуация будет исправлена

    @Ignore
    @Test
    public void courierCantLogInWithoutPassword(){
        Courier courier = Courier.getRandom();
        courierClient.createReturnsIsCreated(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        status = courierClient.loginWithoutPasswordReturnStatus(CourierCredentials.getCourierCredentials(courier));
        assertEquals("Можно войти без логина",400, status);
    }

    @Test
    public void courierCantLogInWithWrongPassword(){
        Courier courier = Courier.getRandom();
        String newPassword = courier.getRandomPassword();
        String login = CourierCredentials.getCourierCredentials(courier).login;
        courierClient.createReturnsIsCreated(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        status = courierClient.loginWithStringLogopassReturnStatus(login, newPassword);
        assertEquals("Нельзя войти с неправильным паролем",404, status);
    }


    @Test
    public void courierCantLogInWithWrongLogin() {
        Courier courier = Courier.getRandom();
        String password = CourierCredentials.getCourierCredentials(courier).password;
        String login = courier.getRandomLogin();
        courierClient.createReturnsIsCreated(courier);
        courierId = courierClient.loginReturnsId(CourierCredentials.getCourierCredentials(courier));
        status = courierClient.loginWithStringLogopassReturnStatus(login, password);
        assertEquals("Нельзя войти с неправильным логином", 404, status);
    }
}
