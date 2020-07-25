package config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private final Properties properties;

    public ConfigLoader(String fileName) throws Exception{
        properties = new Properties();
        loadProperties(fileName);
    }

    private void loadProperties(String fileName) throws Exception{
        InputStream in = new FileInputStream(fileName);
        properties.load(in);
        in.close();
    }

    public String getToken(){
        return properties.getProperty("token");
    }

    public String getPrefix(){
        return properties.getProperty("prefix");
    }
}
