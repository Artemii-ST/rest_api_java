package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lib.Assertions;
import lib.BaseTestCase;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {
    @Test
    public void testGetUserDataNotAuth() {
        BasicConfigurator.configure();
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        Assertions.assertResponseHasKey(responseUserData, "username");
        Assertions.assertResponseNotHasKey(responseUserData, "firstName");
        Assertions.assertResponseNotHasKey(responseUserData, "lastName");
        Assertions.assertResponseNotHasKey(responseUserData, "email");
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser() {
//        BasicConfigurator.configure();
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = getHeader(responseGetAuth, "x-csrf-token");
        String cookie = getCookie(responseGetAuth, "auth_sid");
        ResponseBody responseBody = responseGetAuth.getBody();
        String userId = responseBody.jsonPath().getString("user_id");

        Response responseUserData = RestAssured
                .given()
                .headers("x-csrf-token", header)
                .cookies("auth_sid", cookie)
                .get(String.format("https://playground.learnqa.ru/api/user/%s", userId))
                .andReturn();
        String[] authFields = new String[]{
                "username", "email", "firstName", "lastName"
        };
        Assertions.assertResponseHasFields(responseUserData,authFields);
    }
}

