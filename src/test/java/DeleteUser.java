import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class DeleteUser {
    //private Integer curierId;

    public void deleteUser(String login, String password) {
        if(login != null && login != null ) {


            LoginRequest loginRequest = new LoginRequest(password, login);

            Integer curierId = given()
                    .header("Content-type", "application/json")
                    .body(loginRequest)
                    .when()
                    .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/login")
                    .then()
                    .extract().jsonPath().getInt("id");


                 if(curierId != null) {
                     given()
                             .when()
                             .delete("https://qa-scooter.praktikum-services.ru/api/v1/courier/" + curierId)
                             .then()
                             .statusCode(200)
                             .body("ok", equalTo(true));
                 }

        }
    }
}
