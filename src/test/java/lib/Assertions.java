package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions {
    public static void asserJsonByName(Response response, String name, int expectedValue) {
        response.then().assertThat().body("$", hasKey(name));
        int value = response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equals to expected value");
    }

    public static void assertResponseTextEquals(Response response, String expectedText) {
        assertEquals(
                expectedText,
                response.asString(),
                "Response text is not as expected"
        );
    }

    public static void assertResponseStatusCodeEquals(Response response, int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                response.getStatusCode(),
                "Response status code is not as expected"
        );
    }
}
