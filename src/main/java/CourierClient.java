import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;
public class CourierClient extends RestAssuredClient {
    private static final String COURIER_PATH = "api/v1/courier";

    public Response create(Courier courier) {
        return given()
                .log().all()
                .spec(getBaseSpec())
                .body(courier).when()
                .post(COURIER_PATH);
    }

    public Boolean createReturnsIfOk(Courier courier){
        return given()
                .log().all()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then()
                .extract()
                .path("ok");
    }

    public int createWithoutLoginReturnsStatus(Courier courier) {
        JSONObject registerRequestBodyWithoutLogin = new JSONObject();
        registerRequestBodyWithoutLogin.put("password", courier.password);
        registerRequestBodyWithoutLogin.put("firstName", courier.firstName);
        Response response = given()
                .log().all()
                .spec(getBaseSpec())
                .body(registerRequestBodyWithoutLogin.toString())
                .when()
                .post(COURIER_PATH);
        return response.statusCode();

    }

    public int createWithoutPasswordReturnsStatus(Courier courier) {
        JSONObject registerRequestBodyWithoutPassword = new JSONObject();
        registerRequestBodyWithoutPassword.put("password", courier.password);
        registerRequestBodyWithoutPassword.put("firstName", courier.firstName);
        Response response = given()
                .log().all()
                .spec(getBaseSpec())
                .body(registerRequestBodyWithoutPassword.toString())
                .when()
                .post(COURIER_PATH);
        return response.statusCode();

    }

    public int createWithoutFirstNameReturnsStatus(Courier courier) {
        JSONObject registerRequestBodyWithoutFirstName = new JSONObject();
        registerRequestBodyWithoutFirstName.put("login", courier.login);
        registerRequestBodyWithoutFirstName.put("password", courier.password);
        Response response = given()
                .log().all()
                .spec(getBaseSpec())
                .body(registerRequestBodyWithoutFirstName.toString())
                .when()
                .post(COURIER_PATH);
        return response.statusCode();
    }

    public Response login(CourierCredentials courierCredentials) {
        return given()
                .log().all()
                .spec(getBaseSpec())
                .body(courierCredentials)
                .when()
                .post(COURIER_PATH + "/login");
    }
    public int loginWithStringLogopassReturnStatus(String login, String password) {
        JSONObject loginRequestBody = new JSONObject();
        loginRequestBody.put("login", login);
        loginRequestBody.put("password", password);
        Response response = given()
                .log().all()
                .spec(getBaseSpec())
                .body(loginRequestBody.toString())
                .when()
                .post(COURIER_PATH + "/login");
        return response.statusCode();
    }

    public int loginWithoutLoginReturnStatus(CourierCredentials courierCredentials) {
        JSONObject loginRequestBodyWithoutLogin = new JSONObject();
        loginRequestBodyWithoutLogin.put("password", courierCredentials.password);
        Response response = given()
                .log().all()
                .spec(getBaseSpec())
                .body(loginRequestBodyWithoutLogin.toString())
                .when()
                .post(COURIER_PATH + "/login");
        return response.statusCode();
    }

    public int loginWithoutPasswordReturnStatus(CourierCredentials courierCredentials) {
        JSONObject loginRequestBodyWithoutPassword = new JSONObject();
        loginRequestBodyWithoutPassword.put("login", courierCredentials.login);
        Response response = given()
                .log().all()
                .spec(getBaseSpec())
                .body(loginRequestBodyWithoutPassword.toString())
                .when()
                .post(COURIER_PATH + "/login");
        return response.statusCode();
    }

    public Response deleteCourier(int courierId) {
        return given()
                .log().all()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }

    public int deleteWithoutIdReturnsStatus() {
        Response response = given()
                .log().all()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH + "/");
        return response.statusCode();
    }


}

