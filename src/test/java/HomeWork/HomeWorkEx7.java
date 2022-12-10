package HomeWork;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HomeWorkEx7 {
    @Test
    public void testRestAssured() {
        String URL = "https://playground.learnqa.ru/api/long_redirect";
        while (Integer.parseInt(checkRedirectsAndReturnStatusCode(URL).get("StatusCode")) != 200) {
            URL = checkRedirectsAndReturnStatusCode(URL).get("URL");
            System.out.println(URL);
        }
    }

    public Map<String, String> checkRedirectsAndReturnStatusCode(String url) {
        Map<String, String> result = new HashMap<>();
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get(url)
                .andReturn();

        String getHeaderLocation = response.getHeader("Location");
        Integer statusCode = response.getStatusCode();
        result.put("StatusCode", statusCode.toString());
        result.put("URL", getHeaderLocation);
        return result;
    }
}
