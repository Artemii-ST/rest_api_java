package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Creating a user with a very long username - longer than 250 characters")
    public void testCreateUserWithLongName() {
        Response responseCreateAuth = apiCoreRequests.registrationUserWithNeededFirstName(
                "https://playground.learnqa.ru/api/user/",
                DataGenerator.getRandomString(255));
        System.out.println(responseCreateAuth.asString());
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of '" + VariablesRequests.FIRSTNAME + "' field is too long");
    }

    @Test
    @Description("Creating a user with a very short name of one character")
    public void testCreateUserWithShortName() {
        Response responseCreateAuth = apiCoreRequests.registrationUserWithNeededFirstName(
                "https://playground.learnqa.ru/api/user/",
                "");
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of '" + VariablesRequests.FIRSTNAME + "' field is too short");
    }

    @ParameterizedTest
    @Description("Creating a user without specifying one of the fields")
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void testCreateUserWithOutNeededField(String unnecessary_field) {
        Response responseCreateAuth = apiCoreRequests.registrationRandomUserWithOutNeededField(
                "https://playground.learnqa.ru/api/user/",
                unnecessary_field);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + unnecessary_field);
    }


    @Test
    @Description("Checking user creation with invalid e-mail")
    public void testCreateUserWithWrongEmail() {
        Response responseCreateAuth = apiCoreRequests.registrationUserWithNeededEmail(
                "https://playground.learnqa.ru/api/user/",
                "wrong_email!extra.ru");
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
        Assertions.assertResponseStatusCodeEquals(responseCreateAuth, 400);
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
        Assertions.assertResponseStatusCodeEquals(responseCreateAuth, 200);
        Assertions.assertResponseHasKey(responseCreateAuth, "id");
    }
}
