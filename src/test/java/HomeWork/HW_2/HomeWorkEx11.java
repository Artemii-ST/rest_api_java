package HomeWork.HW_2;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeWorkEx11 {
    @Test
    public void testResponseCookie() {
        BasicConfigurator.configure();
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Map<String , String> cookieFromRequest = response.getCookies();
        Map<String , String> expectedCookie = new HashMap<>();
        expectedCookie.put("HomeWork","hw_value");
        assertEquals(expectedCookie,cookieFromRequest, "The received cookie does not match what was expected");
    }
}
