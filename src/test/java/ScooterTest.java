import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class ScooterTest {
private String URL = "https://qa-scooter.praktikum-services.ru";
   private String login = "ninj4321a";
    private String password = "1234";
    private String firstName = "saske";
//Создание курьера
    @Test
    public void postCourier() {

        LoginUserPojo logonUserPojo = new LoginUserPojo(password, login, firstName);
        Response response = given()
                .header("Content-type", "application/json")
                .body(logonUserPojo)
                .when()
                .post(URL + "/api/v1/courier")
                .then().extract().response();
        response.then().assertThat().statusCode(201).body("ok", equalTo(true));
        //удаляем юзера после теста
        DeleteUser deleteUser = new DeleteUser();
        deleteUser.deleteUser(login,password);
    }
    //Создание курьера
    @Test
    public void postCourierNoPussword(){
String json = "{\n" +
        "    \"login\": \"ninja\",\n" +
        "    \"password\": \"\",\n" +
        "    \"firstName\": \"saske\"\n" +
        "}";
        Specifications specifications = new Specifications();
        Response response = specifications.postCurier(json, URL);
        response.then().assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }
    //Создание курьера
    @Test
    public void postCourierNoLogin(){
        String json = "{\n" +
                "    \"login\": \"\",\n" +
                "    \"password\": \"1234\",\n" +
                "    \"firstName\": \"saske\"\n" +
                "}";
        Specifications specifications = new Specifications();
        Response response = specifications.postCurier(json, URL);
        response.then().assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }
    //Создание курьера
    @Test
    public void postCourierDoubleUser(){
        String json = "{\n" +
                "    \"login\": \"ninja\",\n" +
                "    \"password\": \"1234\",\n" +
                "    \"firstName\": \"saske\"\n" +
                "}";
        Specifications specifications = new Specifications();
        Response response = specifications.postCurier(json, URL);
        response.then().assertThat().statusCode(409).body("message", equalTo("Этот логин уже используется"));

    }
    //Логин курьера
    @Test
    public void curierAutorized() {
        //создаем юзера
        LoginUserPojo logonUserPojo = new LoginUserPojo(password, login, firstName);
       given()
                .header("Content-type", "application/json")
                .body(logonUserPojo)
                .when()
                .post(URL + "/api/v1/courier")
                .then().extract().response();

        //авторизуемся
        LoginRequest loginRequest = new LoginRequest(password, login);
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginRequest)
                .when()
                .post(URL+ "/api/v1/courier/login");
        response.then().assertThat().statusCode(200).body("id", notNullValue());
        //удаляем юзера после теста
        DeleteUser deleteUser = new DeleteUser();
        deleteUser.deleteUser(login,password);
    }
    //Логин курьера
   @Test
    public void curierAutorizedBadRequest(){
       String login = "ninj4321673432431a";
       String password="";
       LoginRequest loginRequest = new LoginRequest(password, login);
       Response response = given()
               .header("Content-type", "application/json")
               .body(loginRequest)
               .when()
               .post(URL+ "/api/v1/courier/login");
       response.then().assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для входа"));
    }
    //Логин курьера
    @Test
    public void curierAutorizedNotFound(){
        String login = "lalala";
        String password="222";
        LoginRequest loginRequest = new LoginRequest(password, login);
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginRequest)
                .when()
                .post(URL+ "/api/v1/courier/login");
        response.then().assertThat().statusCode(404).body("message", equalTo("Учетная запись не найдена"));


    }
    //Создание заказа
    @ParameterizedTest
    @MethodSource("color")
    public void postOrder201(List<String> color){
        ArrayList<String> colorList = new ArrayList<>(color);
        OrdersPojo ordersPojo = new OrdersPojo("Konoha, 142 apt.", colorList,"Saske, come back to Konoha","2020-06-06","Naruto","Uchiha",4,"+7 800 355 35 35",5);
given()
        .body(ordersPojo)
        .when()
        .post(URL + "/api/v1/orders")
        .then().assertThat().statusCode(201).body("track", notNullValue());
    }
    public static Stream<List<String>> color(){
        return Stream.of(
                Arrays.asList("BLACK"),
                Arrays.asList("GREY"),
                Arrays.asList("GREY,BLACK"),
                Arrays.asList("")
        );
    }

    @Test
    /*
    получаем список заказов
    проверяем, что не назначен курьер courierId - пусто
    проверяем, что в ответе 10 заказов
     */
    public void getListOrders(){
       Response response =  given()
                .when()
                .get(URL+ "/api/v1/orders?limit=10&page=0");
        response.then().assertThat()
                .statusCode(200)
                .body("courierId", nullValue())
                .body("orders.size()", equalTo(10));

    }    @Test
    /*
    получаем список заказов
    проверяем, что не назначен курьер courierId - пусто
    проверяем, что в ответе 10 заказов
    проверяем, что рядом со станцией Калужская (110)
     */
    public void getListOrdersMeyto110(){
        Response response =  given()
                .when()
                .get(URL+ "/api/v1/orders?limit=10&page=0&nearestStation=[\"110\"]");
        response.then().assertThat()
                .statusCode(200)
                .body("courierId", nullValue())
                .body("orders.size()", equalTo(10))
                .body("orders.metroStation", everyItem( equalTo("110")));
    }
    /*
    Список заказов
    проверяем активные заказы на станции"Бульвар Рокоссовского"(1)
     */
    @Test
    public void getAllOrdersMetro1(){
        //создаем юзера
        LoginUserPojo logonUserPojo = new LoginUserPojo(password, login, firstName);
        given()
                .header("Content-type", "application/json")
                .body(logonUserPojo)
                .when()
                .post(URL + "/api/v1/courier")
                .then().statusCode(201);
        //авторизуемся
        LoginRequest loginRequest = new LoginRequest(password, login);
         Response response = given()
                .header("Content-type", "application/json")
                .body(loginRequest)
                .when()
                .post(URL + "/api/v1/courier/login");
        response.then().assertThat().statusCode(200);
        int curierId =  response.jsonPath().getInt("id");

        //создаем заказ
        ArrayList<String>color = new ArrayList<>();
        color.add("BLACK");
        OrdersPojo ordersPojo = new OrdersPojo("Konoha, 142 apt.", color,"Saske, come back to Konoha","2020-06-06","Naruto","Uchiha",1,"+7 800 355 35 35",5);
        Response response1 = given()
                .body(ordersPojo)
                .when()
                .post(URL + "/api/v1/orders");
        response1.then().assertThat().statusCode(201);
        int track = response1.jsonPath().getInt("track");
        System.out.println(track);
        // Получаем orderId по track
        int orderId = given()
                .queryParam("t", track)
                .when()
                .get(URL + "/api/v1/orders/track")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getInt("order.id");

        //принимаем заказ
        given()
                .when()
                .queryParam("courierId", curierId)
                .put(URL+ "/api/v1/orders/accept/" + orderId);

        //проверяем активные заказы на станции"Бульвар Рокоссовского"(1)
        System.out.println("айди заказа " + orderId + " номер заказа " + track + " айди курьера " + curierId);
      Response response2 =  given()
                .when()
                .queryParam("courierId",curierId)
                .queryParam("nearestStation","1")
                .get(URL+ "/api/v1/orders");
      response2.then()
                .statusCode(200)
                .body("orders[0].courierId",equalTo(curierId));

        DeleteUser deleteUser = new DeleteUser();
        deleteUser.deleteUser(login,password);
    }




}
