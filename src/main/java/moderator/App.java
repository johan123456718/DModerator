package moderator;

import moderator.config.Bot;

public class App {
    public static void main(String[] args) {
        String fileName = args.length > 0 ? args[0] : "";
        new Bot(fileName).run();
    }
}
