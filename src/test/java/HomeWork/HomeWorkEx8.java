package HomeWork;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;

public class HomeWorkEx8 {
    @Test
    public void testRestAssured() {
        JsonPath responseCreateTask = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        int seconds = responseCreateTask.get("seconds");
        String token = responseCreateTask.get("token");

        JsonPath responseCheckFieldStatus = RestAssured
                .given()
                .queryParams("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String status = responseCheckFieldStatus.get("status");
        System.out.println(status);
        boolean taskIsNotReady = responseCheckFieldStatus.get("status").equals("Job is NOT ready");
        if (taskIsNotReady) {
            try {
                System.out.println(String.format("Gotta wait: %d seconds",seconds));
                TimeUnit.SECONDS.sleep(seconds + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        JsonPath responseTaskReady = RestAssured
                .given()
                .queryParams("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String result = responseTaskReady.get("status");
        System.out.println(result);
    }
}
