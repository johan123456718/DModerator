package moderator.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;

public class Config {

    private final String FILE = "bot.properties";
    private final Properties properties;
    private static String prefix;
    private static String token;
    private static String owner;
    private static String[] mods;

    public Config(String filename)throws Exception{
        properties = filename.isBlank() ? createProperties() : createProperties(filename);
        loadProperties(properties);
    }

    // If custom configuration file is provided
    private Properties createProperties(String fileName) throws Exception{
        Properties prop = new Properties();
        try(InputStream in = new FileInputStream(fileName)){
            prop.load(in);
        }
        return prop;
    }

    // Default configs
    private Properties createProperties() throws Exception{
        Properties prop = new Properties();
        try(InputStream in = defaultPropertiesFile()){
            prop.load(in);
        }
        return prop;
    }

    private InputStream defaultPropertiesFile(){
        return Optional.ofNullable(getClass().getClassLoader().getResourceAsStream(FILE))
                .orElseThrow(() -> new NoSuchElementException("'bot.properties` file is missing or invalid!"));
    }

    private static void loadProperties(Properties properties){
        prefix = properties.getProperty("prefix");
        token = properties.getProperty("token");
        owner = properties.getProperty("owner");
        mods = properties.getProperty("mods").split(",");
    }

    public static String getPrefix(){
        return Optional.ofNullable(prefix).orElseThrow(() -> new NoSuchElementException("Error: Prefix not found!"));
    }

    public static String getToken(){
        return Optional.ofNullable(token).orElseThrow(() -> new NoSuchElementException("Error! Token not found!"));
    }

    public static String getOwner(){
        return Optional.ofNullable(owner).orElseThrow(() -> new NoSuchElementException("Error! Owner ID not found!"));
    }

    public static String[] getMods(){
        return Optional.ofNullable(mods).orElseThrow(() -> new NoSuchElementException("Error! Mod IDs not found!"));
    }
}

