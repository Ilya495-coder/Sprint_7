import courier.CourierApi;
import courier.LoginRequest;
import courier.LoginUserPojo;
import io.restassured.RestAssured;
import order.OrderApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostCourierTest {
    CourierApi courierApi;
    OrderApi orderApi;
    int id;

    @BeforeEach
    public void getUrl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courierApi = new CourierApi();
        orderApi = new OrderApi();
    }

    //Создание курьера
    @DisplayName("Создание курьера, успешный сценарий")
    @Test
    public void postCourier() {
        var courier = LoginUserPojo.random();
        boolean created = courierApi.create(courier).statusCode(201)
                .extract().path("ok");
        assertTrue(created);
        var creds = LoginRequest.fromCourier(courier);
        //авторизуемся чтобы удалить юзера
        this.id = courierApi.logIn(creds).statusCode(200)
                .extract().path("id");

    }

    //Создание курьера с пустым паролем
    @DisplayName("Создание курьера с пустым паролем")
    @Test
    public void postCourierNoPussword() {
        LoginUserPojo loginUserPojo = new LoginUserPojo("", "ninja", "saske");
        courierApi.create(loginUserPojo).statusCode(400).body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    //Создание курьера с пустым логином
    @DisplayName("Создание курьера с пустым логином")
    @Test
    public void postCourierNoLogin() {
        LoginUserPojo loginUserPojo = new LoginUserPojo("1234", "", "saske");
        courierApi.create(loginUserPojo).statusCode(400).body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    //Создание курьера с уже существующим логином
    @DisplayName("Создание курьера с уже существующим логином")
    @Test
    public void postCourierDoubleUser() {
        LoginUserPojo loginUserPojo = new LoginUserPojo("1234", "ninja", "saske");
        courierApi.create(loginUserPojo).statusCode(409).body("message", equalTo("Этот логин уже используется"));

    }

    //удаляем юзера
    @AfterEach
    public void deleteUser() {
        if (id > 0) {
            courierApi.delete(id);
        }
    }
}
