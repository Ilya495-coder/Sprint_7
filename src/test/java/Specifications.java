import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Specifications {
public Response postCurier(String json , String URL){
    Response response = given()
            .header("Content-type", "application/json")
            .body(json)
            .when()
            .post(URL + "/api/v1/courier")
            .then().extract().response();
    return response;
}
}
