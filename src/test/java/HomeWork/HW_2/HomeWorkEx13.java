package HomeWork.HW_2;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeWorkEx13 {
    private String USER_AGENT_1= "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
    private String USER_AGENT_2= "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1";
    private String USER_AGENT_3= "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    private String USER_AGENT_4= "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0";
    private String USER_AGENT_5= "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";

    public void testUserAgentHelp(String url, String stringUserAgent, String expectedPlatform, String expectedBrowser, String expectedDevice) {
        Map<String,String> expect = new HashMap<>();
        expect.put("platform",expectedPlatform);
        expect.put("browser", expectedBrowser);
        expect.put("device",expectedDevice);
        RequestSpecification spec = RestAssured.given();
        spec.baseUri(url);
        spec.headers("user-Agent",stringUserAgent);
        JsonPath response = spec.get().jsonPath();
        for (Map.Entry<String,String> entry : expect.entrySet()) {
            try {
                assertEquals(entry.getValue(),response.getString(entry.getKey()),
                        "we get path " + entry.getKey() + " in ----> " + url + " Received value does not match expected result");
            } catch (Exception e) {
                System.out.println(e);
            }
            continue;
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"userAgent1", "userAgent2","userAgent3","userAgent4","userAgent5"})
    public void testUserAgent(String condition) {
        //BasicConfigurator.configure();
        switch (condition) {
            case "userAgent1" :
                testUserAgentHelp("https://playground.learnqa.ru/ajax/api/user_agent_check",
                        USER_AGENT_1,
                        "Mobile",
                        "No",
                        "Android");
                break;
            case "userAgent2" :
                testUserAgentHelp("https://playground.learnqa.ru/ajax/api/user_agent_check",
                        USER_AGENT_2,
                        "Mobile",
                        "Chrome",
                        "iOS");
                break;
            case "userAgent3" :
                testUserAgentHelp("https://playground.learnqa.ru/ajax/api/user_agent_check",
                        USER_AGENT_3,
                        "Googlebot",
                        "Unknown",
                        "Unknown");
                break;
            case "userAgent4" :
                testUserAgentHelp("https://playground.learnqa.ru/ajax/api/user_agent_check",
                        USER_AGENT_4,
                        "Web",
                        "Chrome",
                        "No");
                break;
            case "userAgent5" :
                testUserAgentHelp("https://playground.learnqa.ru/ajax/api/user_agent_check",
                        USER_AGENT_5,
                        "Mobile",
                        "No",
                        "iPhone");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + condition);
        }
    }
}
