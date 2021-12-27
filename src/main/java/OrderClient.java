import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient{

    private static final String ORDER_PATH = "api/v1/orders";

    public Response createOrderReturnsResponse(Order order){
        return given()
                .log().all()
                .spec(getBaseSpec())
                .body(order)
                .post(ORDER_PATH);
    }

    public Order getRandomOrder(List<String> color){
        Random random = new Random();
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String lastName = faker.name().lastName();
        String address = faker.address().streetAddressNumber();
        String metro = "Чертановская";
        String phone = faker.name().firstName();
        int rentTime = random.nextInt(6)+ 1;
        String comment = faker.name().firstName();

        return new Order(color, name, lastName, address, metro, phone, rentTime, comment );
    }

    public int cancelOrderReturnsStatus (int trackId){
        Response response = given()
                .log().all()
                .spec(getBaseSpec())
                .when()
                .put(ORDER_PATH + "/cancel");
        return response.getStatusCode();
    }
    public Response getOrdersReturnsResponse(){
        return given().spec(getBaseSpec()).log().all().get(ORDER_PATH);
    }

    public List<String> getOrdersReturnsOrders(){
        Response response = given()
                .spec(getBaseSpec())
                .log().all()
                .when()
                .get(ORDER_PATH);
        return response.body().path("orders");
    }
    public Response getOrdersWithLimitAndPageReturnsResponse(int limit, int page){
        return given().spec(getBaseSpec()).log().all().queryParam("limit", limit).queryParam("page", page).get(ORDER_PATH);
    }

    public int acceptOrderReturnsStatus(int orderId, int courierId ){
        Response response =  given().
                log().all().
                spec(getBaseSpec()).
                queryParam("courierId", courierId)
                .put(ORDER_PATH + "/accept/"+ orderId);
        return response.statusCode();
      }


    public int acceptOrderWithNoOrderIdReturnsStatus(int courierId ){
        Response response =  given().
                log().all().
                spec(getBaseSpec()).
                queryParam("courierId", courierId)
                .put(ORDER_PATH + "/accept/");
        return response.statusCode();
    }

    public int acceptOrderWithNoCourierIdReturnsStatus(int orderId ){
        Response response =  given().
                log().all().
                spec(getBaseSpec())
                .put(ORDER_PATH + "/accept/" + orderId);
        return response.statusCode();
    }

    public int acceptOrderWithWrongOrderIdReturnsStatus(String orderId, int courierId) {
        Response response = given().
                log().all().
                spec(getBaseSpec())
                .queryParam("courierId", courierId)
                .put(ORDER_PATH + "/accept/" + orderId);
        return response.statusCode();
    }

      public int getOrderIdByTrack(int trackId){
        Response response = given(). log().all().spec(getBaseSpec()).queryParam("t", trackId).get(ORDER_PATH + "/track");
        return response.getBody().path("order.id");
      }
      public Response getOrderInfoByTrackReturnsResponse(int trackId){
        return given(). log().all().spec(getBaseSpec()).queryParam("t", trackId).get(ORDER_PATH + "/track");
      }

      public int getOrderInfoWithoutTrackReturnsStatus(){
        Response response = given(). log().all().spec(getBaseSpec()).get(ORDER_PATH + "/track");
        return response.getStatusCode();
    }
}
