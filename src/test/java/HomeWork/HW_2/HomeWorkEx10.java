package HomeWork.HW_2;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeWorkEx10 {
    String HEADER_RESPONSE_VARIABLE = "Date";
    @Test
    public void testFor200() {
        //BasicConfigurator.configure();
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        assertTrue(15 < response.getHeader(HEADER_RESPONSE_VARIABLE).length(),
                "Response variable headers " + HEADER_RESPONSE_VARIABLE + " less than 15 characters");
    }
}
