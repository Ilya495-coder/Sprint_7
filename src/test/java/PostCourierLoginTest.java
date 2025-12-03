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

public class PostCourierLoginTest extends BaseTest {

    //Логин курьера
    @DisplayName("Авторизация курьера, успешный сценарий")
    @Test
    public void curierAutorized() {
        //авторизуемся
        int id = courierApi.logIn(creds).statusCode(200).extract().path("id");
        assertTrue(id > 0);
    }

    //Логин курьера с пустым паролем
    @DisplayName("Авторизация курьера с пустым паролем, но правильным логином")
    @Test
    public void curierAutorizedNoPassword() {
        String login = creds.getLogin();
        LoginRequest loginRequest = new LoginRequest("", login);
        courierApi.logIn(loginRequest).statusCode(400).body("message", equalTo("Недостаточно данных для входа"));
    }
    //Логин курьера с пустым паролем
    @DisplayName("Авторизация курьера с пустым логином, но правильным паролем")
    @Test
    public void curierAutorizedNoLogin() {
        String password = creds.getPassword();
        LoginRequest loginRequest = new LoginRequest(password, "");
        courierApi.logIn(loginRequest).statusCode(400).body("message", equalTo("Недостаточно данных для входа"));
    }

    //Логин несуществующего курьера
    @DisplayName("Авторизация не правильный логин, но верный пароль")
    @Test
    public void curierAutorizedNoExistLogin() {
        String password = creds.getPassword();
        LoginRequest loginRequest = new LoginRequest(password, "lalalappppppwwwwwww");
        courierApi.logIn(loginRequest).statusCode(404).body("message", equalTo("Учетная запись не найдена"));

    }

    @DisplayName("Авторизация не правильный пароль, но верный логин")
    @Test
    public void curierAutorizedNoExistPassword() {
        String login = creds.getLogin();
        LoginRequest loginRequest = new LoginRequest("222", login);
        courierApi.logIn(loginRequest).statusCode(404).body("message", equalTo("Учетная запись не найдена"));
    }
}