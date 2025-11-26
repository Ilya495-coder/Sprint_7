import Courier.LoginRequest;
import Courier.LoginUserPojo;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class ScooterTest {

    CourierApi courierApi;
    OrderApi orderApi;
    int id;

    @BeforeEach
    public void getUrl(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courierApi = new CourierApi();
        orderApi = new OrderApi();
    }

//Создание курьера
    @Test
    public void postCourier() {
        var courier = LoginUserPojo.random();
     boolean created =  courierApi.create(courier)
        .extract().path("ok");
     assertTrue(created);
        var creds = LoginRequest.fromCourier(courier);
        //авторизуемся чтобы удалить юзера
        int id = courierApi.logIn(creds)
                        .extract().path("id");

    }
    //Создание курьера с пустым паролем
    @Test
    public void postCourierNoPussword(){
        LoginUserPojo loginUserPojo = new LoginUserPojo("","ninja","saske");
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginUserPojo)
                .when()
                .post("/api/v1/courier")
                .then().extract().response();
        response.then().assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }
    //Создание курьера с пустым логином
    @Test
    public void postCourierNoLogin(){
        LoginUserPojo loginUserPojo = new LoginUserPojo("1234","","saske");
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginUserPojo)
                .when()
                .post("/api/v1/courier")
                .then().extract().response();
        response.then().assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }
    //Создание курьера с уже существующим логином
    @Test
    public void postCourierDoubleUser(){
        LoginUserPojo loginUserPojo = new LoginUserPojo("1234","ninja","saske");
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginUserPojo)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().statusCode(409).body("message", equalTo("Этот логин уже используется"));

    }
    //Логин курьера
    @Test
        public void curierAutorized() {
            var courier = LoginUserPojo.random();
            courierApi.create(courier);
            var creds = LoginRequest.fromCourier(courier);
            //авторизуемся
            int id = courierApi.logIn(creds)
                    .extract().path("id");
            assertTrue(id > 0);
    }
    //Логин курьера с пустым паролем
   @Test
    public void curierAutorizedBadRequest(){
       LoginRequest loginRequest = new LoginRequest("", "ninj4321673432431a");
       Response response = given()
               .header("Content-type", "application/json")
               .body(loginRequest)
               .when()
               .post("/api/v1/courier/login");
       response.then().assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для входа"));
    }
//    //Логин несуществующего курьера
    @Test
    public void curierAutorizedNotFound(){
        String login = "lalala";
        String password="222";
        LoginRequest loginRequest = new LoginRequest(password, login);
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().statusCode(404).body("message", equalTo("Учетная запись не найдена"));
    }
//    //Создание заказа
    @ParameterizedTest
    @MethodSource("color")
    public void orderCreationAllColor(List<String> color){
        ArrayList<String> colorList = new ArrayList<>(color);
        var order = OrdersPojo.getOrder(colorList);
        int track = orderApi.postOrder(order).extract().path("track");
               assertNotNull(track);
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
                .get("/api/v1/orders?limit=10&page=0");
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
                .get("/api/v1/orders?limit=10&page=0&nearestStation=[\"110\"]");
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
        var courier = LoginUserPojo.random();
        boolean created =  courierApi.create(courier).extract().path("ok");
        assertTrue(created);
        var creds = LoginRequest.fromCourier(courier);
        //авторизуемся
        int courierId = courierApi.logIn(creds).extract().path("id");
        //создаем заказ
        ArrayList<String>color = new ArrayList<>();
        color.add("BLACK");
        var order = OrdersPojo.getOrder(color);
        //получаем номер заказа
        int track = orderApi.postOrder(order).extract().path("track");
        // Получаем фйди заказа по номеру
        int orderId = orderApi.getOdderNumber(track).extract().path("order.id");
        assertTrue(orderId>0);
        //принимаем заказ
      boolean resoult =   orderApi.acceptOrder(courierId,orderId).extract().path("ok");
      assertTrue(resoult);
        //проверяем активные заказы на станции"Бульвар Рокоссовского"(1)
        Response response = orderApi.getListOrders(courierId).extract().response();
        System.out.println(response.body().asString());
      int resoultCouruerId = orderApi.getListOrders(courierId).extract().path("orders[0].courierId");
               assertEquals(courierId, resoultCouruerId);
    }
//удаляем юзера
    @AfterEach
    public void deleteUser(){
        if(id > 0){
              courierApi.delete(id);
          }
        }
}