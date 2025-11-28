import courier.CourierApi;
import courier.LoginRequest;
import courier.LoginUserPojo;
import io.restassured.RestAssured;
import order.OrderApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class PostCourierLoginTest {

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


    //Логин курьера
    @DisplayName("Авторизация курьера, успешный сценарий")
    @Test
    public void curierAutorized() {
        //авторизуемся
        this.id = courierApi.logIn(creds).statusCode(200).extract().path("id");
        assertTrue(id > 0);
    }

    //Логин курьера с пустым паролем
    @DisplayName("Авторизация курьера с пустым паролем")
    @Test
    public void curierAutorizedBadRequest() {
        LoginRequest loginRequest = new LoginRequest("", "ninj4321673432431a");
        courierApi.logIn(loginRequest).statusCode(400).body("message", equalTo("Недостаточно данных для входа"));
    }

    //    //Логин несуществующего курьера
    @DisplayName("Авторизация не правильный логин")
    @Test
    public void curierAutorizedNoExistLogin() {
        LoginRequest loginRequest = new LoginRequest("222", "lalala");
        courierApi.logIn(loginRequest).statusCode(404).body("message", equalTo("Учетная запись не найдена"));
    }

    @DisplayName("Авторизация не правильный пароль")
    @Test
    public void curierAutorizedNoExistPassword() {
        LoginRequest loginRequest = new LoginRequest("222", "ninja");
        courierApi.logIn(loginRequest).statusCode(404).body("message", equalTo("Учетная запись не найдена"));
    }

    //удаляем юзера
    @AfterEach
    public void deleteUser() {
        if (id > 0) {
            courierApi.delete(id);
        }
    }
}