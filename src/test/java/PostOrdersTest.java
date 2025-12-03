import io.restassured.RestAssured;
import order.OrderApi;
import order.OrdersPojo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostOrdersTest extends BaseTest{

    //Создание заказа
    @DisplayName("Создание заказа, успешный сценарий")
    @ParameterizedTest
    @MethodSource("color")

    public void orderCreationAllColor(List<String> color) {
        ArrayList<String> colorList = new ArrayList<>(color);
        var order = OrdersPojo.getOrder(colorList);
        int track = orderApi.postOrder(order).statusCode(201).extract().path("track");
        assertNotNull(track);
    }

    public static Stream<List<String>> color() {
        return Stream.of(Arrays.asList("BLACK"), Arrays.asList("GREY"), Arrays.asList("GREY", "BLACK"), Arrays.asList(""));
    }
}