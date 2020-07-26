package moderator.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;

public class Config {
    private final String FILE =  "config/bot.properties";
    private final Properties properties;

    public Config(String filename) throws Exception{
        /*
           fileName can be supplied by main method args;
           if fileName is null or empty,
               then load from default properties file
         */
        properties = filename.isBlank() ? createProperties(/* Default properties */) : createProperties(filename);
    }

    private Properties createProperties() throws Exception{
        Properties prop = new Properties();
        try(InputStream in = defaultPropertiesFile()){
            prop.load(in);
        }
        return prop;
    }

    private InputStream defaultPropertiesFile(){
        return Optional.ofNullable(getClass().getClassLoader().getResourceAsStream(FILE))
                .orElseThrow(() -> new NoSuchElementException("Default '/config/bot.properties; file missing or invalid!"));
    }

    private Properties createProperties(String fileName) throws Exception{
        Properties prop = new Properties();
        try(InputStream in = new FileInputStream(fileName)){
            prop.load(in);
        }
        return prop;
    }

    String loadPrefix(){
        return properties.getProperty("prefix");
    }

    String loadToken(){
        return properties.getProperty("token");
    }

    String loadOwnerId(){
        return properties.getProperty("owner");
    }

    String[] loadModIds(){
        return properties.getProperty("mods").split(",");
    }
}
