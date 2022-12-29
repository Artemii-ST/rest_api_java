package lib;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class GetData extends BaseTestCase{
    @Step("Get data fields from response")
    public Map<String, String> getHeaderCookieUserIDFromResponse(Response response) {
        Map<String, String> data = new HashMap<>();
        data.put(VariablesRequests.TOKEN, getHeader(response, VariablesRequests.TOKEN));
        data.put(VariablesRequests.COOKIES, getCookie(response, VariablesRequests.COOKIES));
        ResponseBody responseBody = response.getBody();
        data.put(VariablesRequests.USER_ID, responseBody.jsonPath().getString(VariablesRequests.USER_ID));
        return data;
    }

    @Step("Get email and password from user data")
    public Map<String, String> getEmailAndPasswordFromMapData(Map<String, String> userData) {
        Map<String, String> authData = new HashMap<>();
        authData.put(VariablesRequests.EMAIL, userData.get(VariablesRequests.EMAIL));
        authData.put(VariablesRequests.PASSWORD, userData.get(VariablesRequests.PASSWORD));
        return authData;
    }

}
