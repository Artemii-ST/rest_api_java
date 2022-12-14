package TestsCodeResponse;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestsStatusCodeResponse {
    @Test
    public void testFor200() {
        //BasicConfigurator.configure();
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        assertEquals(200, response.getStatusCode(),"Unexpected status code");
    }

    @Test
    public void testFor301() {
        //BasicConfigurator.configure();
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/get_301")
                .andReturn();
        assertEquals(301, response.getStatusCode(),"Unexpected status code");
    }
}
