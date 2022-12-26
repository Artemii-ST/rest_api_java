package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {
    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email",email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" +email+ "' already exists");
        Assertions.assertResponseStatusCodeEquals(responseCreateAuth,400);
    }

    @Test
    public void testCreateUserSuccessfully() {
        String email = DataGenerator.getRandomEmail();
        Map<String, String> date = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(date)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        Assertions.assertResponseStatusCodeEquals(responseCreateAuth,200);
        Assertions.assertResponseHasKey(responseCreateAuth,"id");
    }
}
