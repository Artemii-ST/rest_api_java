package HomeWork.HW_1;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HomeWorkEx6 {
    @Test
    public void testRestAssured() {
        String URL = "https://playground.learnqa.ru/api/long_redirect";
        while (checkRedirectsAndReturnUrl(URL) != null) {
            URL = checkRedirectsAndReturnUrl(URL);
            System.out.println(URL);
        }
    }
    public String checkRedirectsAndReturnUrl(String url) {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get(url)
                .andReturn();

        String getHeaderLocation = response.getHeader("Location");
        return getHeaderLocation;
    }
}
