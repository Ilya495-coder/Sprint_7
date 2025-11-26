import Courier.LoginRequest;
import Courier.LoginUserPojo;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierApi {

    private String URL = "https://qa-scooter.praktikum-services.ru";


    @Step("Идентификация юзера")
    public ValidatableResponse logIn(LoginRequest loginRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post(URL + "/api/v1/courier/login")
                .then().log().all();
    }

@Step("Метод создания юзера")
    public ValidatableResponse create(LoginUserPojo courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(URL + "/api/v1/courier")
                .then().log().all();
    }
    @Step("Удаление юзера")
    public ValidatableResponse delete(int id ){
        return given()
                .when()
                .delete("https://qa-scooter.praktikum-services.ru/api/v1/courier/" + id)
                .then().log().all();
    }
}