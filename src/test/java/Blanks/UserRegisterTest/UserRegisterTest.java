package Blanks.UserRegisterTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {
    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        Map<String, String> date = new HashMap<>();
        date.put("email",email);
        date.put("password", "123");
        date.put("username","Loni_kot");
        date.put("firstName", "Loni");
        date.put("lastName", "Kotov");

        Response responseCreateAuth = RestAssured
                .given()
                .body(date)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" +email+ "' already exists");
        Assertions.assertResponseStatusCodeEquals(responseCreateAuth,400);
    }
}
