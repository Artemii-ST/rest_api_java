package lib;

import java.text.SimpleDateFormat;
import java.util.Random;

public class DataGenerator {
    public static String getRandomEmail() {
        String random = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "email" + random + "@example.com";
    }
}
