import courier.CourierApi;
import courier.LoginRequest;
import courier.LoginUserPojo;
import io.restassured.RestAssured;
import order.OrderApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;



public class BaseTest {

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
        this.id = courierApi.logIn(creds).extract().path("id");

    }
    //удаляем юзера
    @AfterEach
    public void deleteUser() {
        if (id > 0) {
            courierApi.delete(id);
        }
    }
}
