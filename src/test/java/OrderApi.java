import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderApi {
    private String URL = "https://qa-scooter.praktikum-services.ru";

    @Step("Создание заказа")
    public ValidatableResponse postOrder(OrdersPojo ordersPojo){
        return given()
                .contentType(ContentType.JSON)
                .body(ordersPojo)
                .when()
                .post(URL + "/api/v1/orders")
                .then().log().all()
                .assertThat().statusCode(201);

    }
    @Step("Получить заказ по его номеру")
    public ValidatableResponse getOdderNumber(int track){
       return given()
                .queryParam("t", track)
                .when()
                .get("/api/v1/orders/track")
                .then()
                .statusCode(200).log().all();

    }
    @Step("Принять заказ")
    public ValidatableResponse acceptOrder(int courierId , int track){
        return given()
                .when()
                .queryParam("courierId", courierId)
                .put("/api/v1/orders/accept/" + track)
                .then()
                .statusCode(200)
                .log().all();

    }
    @Step("Получение списка заказов")
    public ValidatableResponse getListOrders(int courierId){
        return given()
                .when()
                .queryParam("courierId",courierId)
                .queryParam("nearestStation","1")
                .get("/api/v1/orders")
                .then()
                .statusCode(200).log().all();
    }
}
