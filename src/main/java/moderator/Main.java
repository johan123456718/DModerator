package moderator;


import moderator.config.Bot;

public class Main {
    public static void main(String[] args) {
        String filenameProperties = args.length > 0 ? args[0]: "";
        new Bot(filenameProperties).run();
    }
}
