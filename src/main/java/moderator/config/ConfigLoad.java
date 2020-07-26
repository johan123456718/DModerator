package moderator.config;

import java.util.NoSuchElementException;
import java.util.Optional;

public class ConfigLoad{
    private static String prefix;
    private static String token;
    private static String owner;
    private static String[] mods;

    public static void load(String fileName){
        try{
            Config config = new Config(fileName);
            loadProperties(config);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void loadProperties(Config config){
        prefix = config.loadPrefix();
        token = config.loadToken();
        owner = config.loadOwnerId();
        mods = config.loadModIds();
    }

    public static String prefix(){
        return Optional.ofNullable(prefix).orElseThrow(() -> new NoSuchElementException("Error: Prefix invalid!"));
    }

    public static String token(){
        return Optional.ofNullable(token).orElseThrow(() -> new NoSuchElementException("Error! Token invalid!"));
    }

    public static String owner(){
        return Optional.ofNullable(owner).orElseThrow(() -> new NoSuchElementException("Error! Owner ID invalid!"));
    }

    public static String[] mods(){
        return Optional.ofNullable(mods).orElseThrow(() -> new NoSuchElementException("Error! Mod IDs invalid!"));
    }
}

