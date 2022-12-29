package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lib.*;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserGetTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testGetUserDetailsAuthAsOtherUser() {
        BasicConfigurator.configure();
        //Create User
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseRegUser = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData
        );
        //Login user
        Response responseLoginUser = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                new GetData().getEmailAndPasswordFromMapData(userData)
        );
        //Check user data
        Response responseUserData = apiCoreRequests.creatingRequestWithParam(
                "https://playground.learnqa.ru/api/user/%s",
                responseLoginUser
        );
        assertTrue(Assertions.assertResponseTextEqualsBoolean(responseUserData, "User not found")
                        || Assertions.assertResponseHasFieldBoolean(responseUserData, VariablesRequests.USERNAME),
                "request received erroneous data ==> " + responseUserData.asString());
        String[] notExpectedFields = new String[]{
                VariablesRequests.FIRSTNAME,
                VariablesRequests.LASTNAME,
                VariablesRequests.EMAIL
        };
        Assertions.assertResponseNotHasFields(responseUserData, notExpectedFields);

    }

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
        Assertions.assertResponseHasFields(responseUserData, authFields);
    }
}

