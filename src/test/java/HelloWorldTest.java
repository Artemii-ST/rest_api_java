import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {



    @Test
    public void testRestAssured() {
        BasicConfigurator.configure();
        Map<String, String> date = new HashMap<>();
        date.put("login", "secret_login2");
        date.put("password", "secret_pass");

        Response responseForGet = RestAssured
                .given()
                .body(date)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie");

        Map<String, String> cookies = new HashMap<>();
        if (responseCookie != null) {
            cookies.put("auth_cookie", responseCookie);
        }

        Response responseForCheck = RestAssured
                .given()
                .body(date)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseForCheck.print();
    }
}
