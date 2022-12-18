package HomeWork.HW_2;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeWorkEx12 {
    @Test
    public void testResponseHeader() {
        BasicConfigurator.configure();
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        Headers headers = response.getHeaders();
        String expectedHeader = "application/json";
        assertEquals(expectedHeader, response.getHeader("Content-Type"), "Received header from does not match expected result");
    }
}
