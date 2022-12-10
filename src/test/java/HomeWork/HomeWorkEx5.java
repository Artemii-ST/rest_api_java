package HomeWork;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.List;

public class HomeWorkEx5 {
    @Test
    public void testRestAssured() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        List jsonGet = response.get("messages");
        System.out.println(jsonGet.get(1));
    }
}
