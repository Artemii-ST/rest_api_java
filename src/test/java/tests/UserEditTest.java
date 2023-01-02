package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.*;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {
    @Test
    @Description("change the firstName of the user, being authorized by the same user, to a very short value of one character")
    public void testEditUserForShortFirstName() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = new ApiCoreRequests().makeUserCreatePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData);
        String userID = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new GetData().getEmailAndPasswordFromMapData(userData);
        Response responseGetAuth = new ApiCoreRequests().makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData
        );

        //EDIT
        String newFirstName = "X";
        Map<String, String> editData = new HashMap<>();
        editData.put(VariablesRequests.FIRSTNAME, newFirstName);

        Response responseEditUser = new ApiCoreRequests().makePostRequestForEditUser(
                "https://playground.learnqa.ru/api/user/",
                responseGetAuth,
                editData,
                userID
        );
        Assertions.asserJsonByName(responseEditUser,"error","Too short value for field firstName");

    }

    @Test
    @Description("change the user's email, being authorized by the same user, to a new email without the @ symbol")
    public void testEditUserForWrongEmail() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = new ApiCoreRequests().makeUserCreatePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData);
        String userID = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new GetData().getEmailAndPasswordFromMapData(userData);
        Response responseGetAuth = new ApiCoreRequests().makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData
        );

        //EDIT
        String newEmail = userData.get(VariablesRequests.EMAIL).replace('@','#');
        Map<String, String> editData = new HashMap<>();
        editData.put(VariablesRequests.EMAIL, newEmail);

        Response responseEditUser = new ApiCoreRequests().makePostRequestForEditUser(
                "https://playground.learnqa.ru/api/user/",
                responseGetAuth,
                editData,
                userID
        );
        Assertions.assertResponseTextEquals(responseEditUser,"Invalid email format");

    }

    @Test
    @Description("change user data while being authorized by another user")
    public void testEditUserWithAuthorizationOtherUser() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = new ApiCoreRequests().makeUserCreatePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData
        );
        String userID = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new GetData().getEmailAndPasswordFromMapData(userData);
        Response responseGetAuth = new ApiCoreRequests().makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData
        );

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put(VariablesRequests.FIRSTNAME, newName);

        Response responseEditUser;
        responseEditUser = new ApiCoreRequests().makePostRequestForEditUser(
                "https://playground.learnqa.ru/api/user/",
                responseGetAuth,
                editData,
                (Integer.parseInt(userID) - 2)
        );
        //GET
        Response responseUserData = new ApiCoreRequests().makeGetRequest(
                "https://playground.learnqa.ru/api/user/",
                responseGetAuth,
                (Integer.parseInt(userID) - 2)
        );
        Assertions.assertResponseNotHasKey(responseUserData, VariablesRequests.FIRSTNAME);
    }

    @Test
    @Description("change user details while being unauthorized")
    public void testEditUserWithOutAuthorization() {
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = new ApiCoreRequests().makePostRequestForEditUserWithOutAuthData(
                "https://playground.learnqa.ru/api/user/",
                editData,
                "2"
        );
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");

    }

    @Test
    @Description("change user firstName")
    public void testEditJustCreatedTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = new ApiCoreRequests().makeUserCreatePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData
        );
        String userID = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new GetData().getEmailAndPasswordFromMapData(userData);
        Response responseGetAuth = new ApiCoreRequests().makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData
        );

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put(VariablesRequests.FIRSTNAME, newName);

        Response responseEditUser;
        responseEditUser = new ApiCoreRequests().makePostRequestForEditUser(
                "https://playground.learnqa.ru/api/user/",
                responseGetAuth,
                editData,
                userID
        );

        //GET
        Response responseUserData = new ApiCoreRequests().makeGetRequest(
                "https://playground.learnqa.ru/api/user/",
                responseGetAuth,
                userID
        );
        Assertions.asserJsonByName(responseUserData, "firstName", newName);

    }
}
