package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.*;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDeleteTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("try removing the default user")
    public void testTryRemoveDefaultUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(
                        "https://playground.learnqa.ru/api/user/login",
                        authData
                );
        Response deleteUser = apiCoreRequests.makeDeleteRequestForDeleteUser(
                "https://playground.learnqa.ru/api/user/",
                responseGetAuth,
                String.valueOf(getIntFromJson(responseGetAuth, VariablesRequests.USER_ID))
        );
        Assertions.assertResponseTextEquals(deleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description("Create and remove user, make sure the user is deleted")
    public void testRemoveSomeUser() {
        Map<String, String> registrationData = DataGenerator.getRegistrationData();
        Map<String, String> authData = new GetData().getEmailAndPasswordFromMapData(registrationData);
        //Create user
        Response responseRegistration = apiCoreRequests
                .makePostRequest(
                        "https://playground.learnqa.ru/api/user/",
                        registrationData
                );
        //Login user
        Response responseLogin = apiCoreRequests
                .makePostRequest(
                        "https://playground.learnqa.ru/api/user/login",
                        authData
                );
        //Delete user
        Response deleteUser = apiCoreRequests.makeDeleteRequestForDeleteUser(
                "https://playground.learnqa.ru/api/user/",
                responseLogin,
                String.valueOf(getIntFromJson(responseLogin, VariablesRequests.USER_ID))
        );
        Assertions.assertResponseStatusCodeEquals(deleteUser, 200);
        //Check exist user
        Response responseCheckExistUser = new ApiCoreRequests().makeGetRequest(
                "https://playground.learnqa.ru/api/user/",
                responseLogin,
                String.valueOf(getIntFromJson(responseLogin, VariablesRequests.USER_ID))
        );
        Assertions.assertResponseTextEquals(responseCheckExistUser, "User not found");
    }

    @Test
    @Description("Delete a user while being authorized by another user.")
    public void testRemoveUserWithAuthDataOtherUser() {
        Map<String, String> registrationData = DataGenerator.getRegistrationData();
        Map<String, String> authData = new GetData().getEmailAndPasswordFromMapData(registrationData);
        //Create user
        Response responseRegistration = apiCoreRequests
                .makePostRequest(
                        "https://playground.learnqa.ru/api/user/",
                        registrationData
                );
        //Login user
        Response responseLogin = apiCoreRequests
                .makePostRequest(
                        "https://playground.learnqa.ru/api/user/login",
                        authData
                );
        //Delete user
        Response deleteUser = apiCoreRequests.makeDeleteRequestForDeleteUser(
                "https://playground.learnqa.ru/api/user/",
                responseLogin,
                String.valueOf(getIntFromJson(responseLogin, VariablesRequests.USER_ID) - 3)
        );
        //Check exist user
        Response responseCheckExistUser = new ApiCoreRequests().makeGetRequest(
                "https://playground.learnqa.ru/api/user/",
                responseLogin,
                String.valueOf(getIntFromJson(responseLogin, VariablesRequests.USER_ID) - 3)
        );
        assertTrue(Assertions.assertResponseTextEqualsBoolean(responseCheckExistUser, "User not found")
                        || Assertions.assertResponseHasFieldBoolean(responseCheckExistUser, VariablesRequests.USERNAME),
                "request received erroneous data ==> " + responseCheckExistUser.asString());

    }
}
