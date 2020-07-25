import config.ConfigLoader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {
    public static JDA jda;
    public static String prefix;

    public static void main(String[] args) {
        String fileName = "src/main/resources/bot.properties";
        try {
            ConfigLoader config = new ConfigLoader(fileName);
            prefix = config.getPrefix();
            jda = JDABuilder.createDefault(config.getToken())
                    .build();
            jda.awaitReady();
            System.out.println("Finished Building JDA");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
