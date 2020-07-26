package moderator;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.commands.WarningCommand;
import moderator.config.ConfigLoader;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static JDA jda;
    public static String prefix;

    public static void main(String[] args) {
        String fileName = "src/main/resources/owner.txt";
        try {
            ConfigLoader config = new ConfigLoader(fileName);
            prefix = config.getPrefix();

            List<String> list = Files.readAllLines(Paths.get(fileName));
            String token = list.get(0);
            String ownerId = list.get(1);

            EventWaiter waiter = new EventWaiter();
            CommandClientBuilder client = new CommandClientBuilder();

            // The default is "Type !!help" (or whatver prefix you set)
            client.useDefaultGame();

            // sets the owner of the bot
            client.setOwnerId(ownerId);

            // sets emojis used throughout the bot on successes, warnings, and failures
            client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");

            // sets the bot prefix
            client.setPrefix(list.get(2));
            client.addCommands(
                    new WarningCommand(waiter)
            );

            new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .addEventListeners(waiter, client.build())
                    .build();

            System.out.println("Finished Building JDA");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
