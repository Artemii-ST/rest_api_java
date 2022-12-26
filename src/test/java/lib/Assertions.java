package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions {
    public static void asserJsonByName(Response response, String name, int expectedValue) {
        response.then().assertThat().body("$", hasKey(name));
        int value = response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equals to expected value");
    }

    public static void asserJsonByName(Response response, String name, String expectedValue) {
        response.then().assertThat().body("$", hasKey(name));
        String value = response.jsonPath().getString(name);
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

    public static void assertResponseHasKey(Response response, String expectedFieldName) {
        response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertResponseNotHasKey(Response response, String unexpectedFieldName) {
        response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    public static void assertResponseHasFields(Response response, String[] expectedFieldsName) {
        for (String fieldsName : expectedFieldsName) {
            response.then().assertThat().body("$",hasKey(fieldsName));
        }
    }
}
