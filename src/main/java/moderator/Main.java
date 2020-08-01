package moderator;


import moderator.config.BotConfig;

public class Main {
    public static void main(String[] args) {
        String filenameProperties = args.length > 0 ? args[0]: "";
        try{
            new BotConfig(filenameProperties).run();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
