import courier.CourierApi;
import courier.LoginRequest;
import courier.LoginUserPojo;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.OrderApi;
import order.OrdersPojo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetListOrdersTest {
    CourierApi courierApi;
    OrderApi orderApi;
    int id;
    LoginRequest creds;

    @BeforeEach
    public void getUrl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courierApi = new CourierApi();
        orderApi = new OrderApi();
        var courier = LoginUserPojo.random();
        courierApi.create(courier);
        creds = LoginRequest.fromCourier(courier);

    }

    /*
получаем список заказов
проверяем, что не назначен курьер courierId - пусто
проверяем, что в ответе 10 заказов
 */
    @DisplayName("Получение списка свободных заказов, успешный сценарий")
    @Test
    public void getListOrdersCode200() {
        orderApi.getOpenOrders().assertThat().statusCode(200).body("courierId", nullValue()).body("orders.size()", equalTo(10));
    }

    /*
    получаем список заказов
    проверяем, что не назначен курьер courierId - пусто
    проверяем, что в ответе 10 заказов
    проверяем, что рядом со станцией Калужская (110)
     */
    @DisplayName("Получение списка свободных заказов на станции Калужская, успешный сценарий")
    @Test
    public void getListOrdersMeyto110() {
        orderApi.getListOrdersMeyto110().assertThat().statusCode(200).body("courierId", nullValue()).body("orders.size()", equalTo(10)).body("orders.metroStation", everyItem(equalTo("110")));
    }

    /*
    Список заказов
    проверяем активные заказы на станции"Бульвар Рокоссовского"(1)
     */
    @DisplayName("Получение заказов на станции (1), успешный сценарий")
    @Test
    public void getAllOrdersMetro1() {
        //создаем юзера
        var courier = LoginUserPojo.random();
        boolean created = courierApi.create(courier).extract().path("ok");
        assertTrue(created);
        var creds = LoginRequest.fromCourier(courier);
        //авторизуемся
        this.id = courierApi.logIn(creds).extract().path("id");
        //создаем заказ
        ArrayList<String> color = new ArrayList<>();
        color.add("BLACK");
        var order = OrdersPojo.getOrder(color);
        //получаем номер заказа
        int track = orderApi.postOrder(order).extract().path("track");
        // Получаем фйди заказа по номеру
        int orderId = orderApi.getOdderNumber(track).extract().path("order.id");
        assertTrue(orderId > 0);
        //принимаем заказ
        boolean resoult = orderApi.acceptOrder(id, orderId).extract().path("ok");
        assertTrue(resoult);
        //проверяем активные заказы на станции"Бульвар Рокоссовского"(1)
        Response response = orderApi.getListOrders(id).extract().response();
        System.out.println(response.body().asString());
        int resoultCouruerId = orderApi.getListOrders(id).extract().path("orders[0].courierId");
        assertEquals(id, resoultCouruerId);
    }

    //удаляем юзера
    @AfterEach
    public void deleteUser() {
        if (id > 0) {
            courierApi.delete(id);
        }
    }
}
