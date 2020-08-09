package moderator.config;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;

public class Config {

    private final String FILE = "bot.properties";
    private final Properties properties;
    private static String prefix;
    private static String token;
    private static String owner;
    private static String modRoleName;
    private static String[] coOwners;
    private static int defaultCooldown;

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
        coOwners = properties.getProperty("mods").split(",");
        defaultCooldown = Integer.parseInt(properties.getProperty("cooldown"));
        modRoleName = properties.getProperty("mod_role");
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

    public static String[] getCoOwners(){
        return Optional.ofNullable(coOwners).orElseThrow(() -> new NoSuchElementException("Error! Mod IDs not found!"));
    }
    public static int getDefaultCooldown(){
        return Optional.ofNullable(defaultCooldown).orElseThrow(() -> new NoSuchElementException("Error! Cooldown not found!"));
    }
    public static String getModRoleName(){
        return Optional.ofNullable(modRoleName).orElseThrow(() -> new NoSuchElementException("Error! Role name not found!"));
    }

    // Risbah: Just testing this out, will delete/ modify later
    public static Command.Category getModCategory(){
        String categoryName = "Moderation";
        String failureResponse = "You're not allowed to use this command!";
        Predicate<CommandEvent> checkIfAllowed =  event -> {
            boolean userHasPermission = event.getMember().getPermissions().contains(Permission.ADMINISTRATOR);
            return userHasPermission;
        };

        return new Command.Category("Moderation", checkIfAllowed);
    }

    public static Command.Category getGeneralCategory(){
        return new Command.Category("General");
    }
}

