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
        properties = filename.isBlank() ? createProperties() : createProperties(filename);
        Load.prefix = loadPrefix(); // Static prefix to be used throughout the application
        Load.token = loadToken();
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

    private String loadPrefix(){
        return properties.getProperty("prefix");
    }

    private String loadToken(){
        return properties.getProperty("token");
    }

    // nested static class created for better organization
    public static class Load{
        private static String prefix;
        private static String token;

        // Create Config object; only meant for main method
        // Not sure if use of Optional is necessary here,
        // since method terminates if Config is not created
        // and null cant be returned
        public static Config instance(String fileName){
            Optional<Config> config = Optional.empty();

            try{
                config = Optional.of(new Config(fileName));
            } catch (Exception e){
                System.out.println(e.getMessage());
            }

            return config.orElseThrow();
        }

        // Static prefix to be used throughout the application
        public static String prefix(){
            return Optional.ofNullable(prefix).orElseThrow(() -> new NoSuchElementException("Error! Prefix is not loaded! Please run `Config.loadConfig()`"));
        }

        public static String token(){
            return Optional.ofNullable(token).orElseThrow(() -> new NoSuchElementException("Error! Token is not loaded! Please run `Config.loadConfig()"));
        }
    }
}
