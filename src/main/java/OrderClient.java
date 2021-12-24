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
        JSONObject createOrderBody = new JSONObject();
        createOrderBody.put("firstName", order.firstName);
        createOrderBody.put("lastName", order.lastName);
        createOrderBody.put("address", order.address);
        createOrderBody.put("metroStation", order.metroStation);
        createOrderBody.put("phone", order.phone);
        createOrderBody.put("rentTime", order.rentTime);
        createOrderBody.put("deliveryDate", order.deliveryDate);
        createOrderBody.put("comment", order.comment);
        createOrderBody.put("color", order.color);

        return given()
                .log().all()
                .spec(getBaseSpec())
                .body(createOrderBody.toString())
                .post(ORDER_PATH);
    }
    public String getRandomMetro(){
        Random r = new Random();
        Response response = given().get("https://qa-scooter.praktikum-services.ru/api/v1/stations/search").then().contentType(ContentType.JSON).extract().response();
        List<String> jsonResponse = response.jsonPath().getList("$");
        int randomMetro = r.nextInt(jsonResponse.size()-1);
        return response.jsonPath().getString("name[" + randomMetro+ "]");
    }

    public Order getRandomOrder(List<String> color){
        Random random = new Random();
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String lastName = faker.name().lastName();
        String address = faker.address().streetAddressNumber();
        String metro = getRandomMetro();
        String phone = faker.name().firstName();
        int rentTime = random.nextInt(6)+ 1;
        String comment = faker.name().firstName();

        return new Order(color, name, lastName, address, metro, phone, rentTime, comment );
    }

    public void cancelOrder (int trackId){
        JSONObject cancel = new JSONObject();
        cancel.put("track", trackId);
        given()
                .log().all()
                .spec(getBaseSpec())
                .body(cancel.toString())
                .when()
                .put(ORDER_PATH + "/cancel")
                .then()
                .assertThat()
                .statusCode(200);
    }
    public int cancelOrderReturnsStatus (int trackId){
        JSONObject cancel = new JSONObject();
        cancel.put("track", trackId);
        Response response = given()
                .log().all()
                .spec(getBaseSpec())
                .body(cancel.toString())
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
        return given().spec(getBaseSpec()).log().all().get(ORDER_PATH + "?limit=" + limit + "&page=" + page);
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
