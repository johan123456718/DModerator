package moderator;


import moderator.config.BotConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private final static Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        String filenameProperties = args.length > 0 ? args[0]: "";
        try{
            new BotConfig(filenameProperties).run();
        } catch(Exception e){
            LOGGER.fatal(e.getMessage());
        }
    }
}
