package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests extends BaseTestCase {
    @Step("Creating a request with header, cookies and userID")
    public Response creatingRequestWithParam(String url, Response response) {
        return given()
                .headers(VariablesRequests.TOKEN
                        , new GetData().getHeaderCookieUserIDFromResponse(response).get(
                                VariablesRequests.TOKEN
                        ))
                .cookies(VariablesRequests.COOKIES
                        , new GetData().getHeaderCookieUserIDFromResponse(response).get(
                                VariablesRequests.COOKIES
                        ))
                .get(String.format(url, (int) (Math.random()
                        * (Integer.parseInt(new GetData().getHeaderCookieUserIDFromResponse(response).get(
                                VariablesRequests.USER_ID
                )) * 2))))
                .andReturn();
    }

    @Step("Creating a user with the desired firstName")
    public Response registrationUserWithNeededFirstName(String url, String firstName) {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.put(VariablesRequests.FIRSTNAME, firstName);
        return given()
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Creating a random user without a required field")
    public Response registrationRandomUserWithOutNeededField(String url, String unnecessary_field) {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.remove(unnecessary_field);
        return given()
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Creating a user with the desired e-mail")
    public Response registrationUserWithNeededEmail(String url, String email) {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.put("email", email);
        return given()
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookies) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header(VariablesRequests.TOKEN, token))
                .cookies(VariablesRequests.COOKIES, cookies)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookies) {
        return given()
                .filter(new AllureRestAssured())
                .cookies(VariablesRequests.COOKIES, cookies)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header(VariablesRequests.TOKEN, token))
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a POST-request for registration user")
    public JsonPath makeUserCreatePostRequest(String url, Map<String, String> generatedUserData) {
        return given()
                .body(generatedUserData)
                .post(url)
                .jsonPath();
    }

    @Step("Make a POST-request for edit user")
    public Response makePostRequestForEditUser(
            String url,
            Response responseForGetHeaderAndCookies,
            Map<String, String> editedUserData,
            String userID) {
        return RestAssured.given()
                .header(VariablesRequests.TOKEN, this.getHeader(responseForGetHeaderAndCookies, VariablesRequests.TOKEN))
                .cookies(VariablesRequests.COOKIES, getCookie(responseForGetHeaderAndCookies, VariablesRequests.COOKIES))
                .body(editedUserData)
                .put(url + userID)
                .andReturn();
    }

    @Step("Make a DELETE-request for delete user")
    public Response makeDeleteRequestForDeleteUser(
            String url,
            Response responseForGetHeaderAndCookies,
            String userID) {
        return RestAssured.given()
                .header(VariablesRequests.TOKEN, this.getHeader(responseForGetHeaderAndCookies, VariablesRequests.TOKEN))
                .cookies(VariablesRequests.COOKIES, getCookie(responseForGetHeaderAndCookies, VariablesRequests.COOKIES))
                .delete(url + userID)
                .andReturn();
    }

    @Step("Make a POST-request for edit user - with integer userID")
    public Response makePostRequestForEditUser(
            String url,
            Response responseForGetHeaderAndCookies,
            Map<String, String> editedUserData,
            int userID) {
        return RestAssured.given()
                .header(VariablesRequests.TOKEN, this.getHeader(responseForGetHeaderAndCookies, VariablesRequests.TOKEN))
                .cookies(VariablesRequests.COOKIES, getCookie(responseForGetHeaderAndCookies, VariablesRequests.COOKIES))
                .body(editedUserData)
                .put(url + userID)
                .andReturn();
    }

    @Step("Make a POST-request for edit user with out auth data")
    public Response makePostRequestForEditUserWithOutAuthData(
            String url,
            Map<String, String> editedUserData,
            String userID) {
        return RestAssured.given()
                .body(editedUserData)
                .put(url + userID)
                .andReturn();
    }


    @Step("Make a GET-request with token, auth cookie and userID")
    public Response makeGetRequest(String url, Response responseForGetHeaderAndCookies, String userID) {
        return RestAssured
                .given()
                .header(VariablesRequests.TOKEN, getHeader(responseForGetHeaderAndCookies, VariablesRequests.TOKEN))
                .cookies(VariablesRequests.COOKIES, getCookie(responseForGetHeaderAndCookies, VariablesRequests.COOKIES))
                .get(url + userID)
                .andReturn();
    }

    @Step("Make a GET-request with token, auth cookie and userID - with integer userID")
    public Response makeGetRequest(String url, Response responseForGetHeaderAndCookies, int userID) {
        return RestAssured
                .given()
                .header(VariablesRequests.TOKEN, getHeader(responseForGetHeaderAndCookies, VariablesRequests.TOKEN))
                .cookies(VariablesRequests.COOKIES, getCookie(responseForGetHeaderAndCookies, VariablesRequests.COOKIES))
                .get(url + userID)
                .andReturn();
    }

}
