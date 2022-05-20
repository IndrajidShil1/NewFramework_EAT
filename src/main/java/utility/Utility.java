package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

public class Utility {

    public static final String root = System.getProperty("user.dir");

    public static Properties loadProperties(){
        Properties prop = new Properties();

        try {
            InputStream inputStream = new FileInputStream(root+"/src/config.properties");
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public static String decode(String key){
        byte[] decodedBytes = Base64.getDecoder().decode(key);
        return new String(decodedBytes);
    }
}
