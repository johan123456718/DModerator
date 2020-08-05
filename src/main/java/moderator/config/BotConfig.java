package moderator.config;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.moderation.manual.commands.*;
import moderator.moderation.auto.filter.AntiSpamFilter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.NoSuchElementException;
import java.util.Optional;

public class BotConfig {
    private final static Logger LOGGER = LogManager.getLogger(BotConfig.class);
    private static JDA jda;
    private final EventWaiter eventWaiter;

    public BotConfig(String fileName) throws Exception{
        Config config = new Config(fileName);
        eventWaiter = new EventWaiter();
        LOGGER.info("Finish loading bot configurations!");
    }

    public void run(){
        try {
            buildJDA();
            String botName =  getJDA().getSelfUser().getName();
            LOGGER.info(botName + " is ready to work!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // To-DO: MUST ADD :owoPolice: emoji to bot ACTIVITY!!! :)
    private CommandClient buildCommandClient(){
        return new CommandClientBuilder()
                    .setPrefix(Config.getPrefix())
                    .setActivity(Activity.of(Activity.ActivityType.WATCHING,"You Criminals!"))
                    .setOwnerId(Config.getOwner())
                    .setCoOwnerIds(Config.getMods())
                    .addCommands(
                            new UserInfoCommand(eventWaiter),
                            new AdjustNickNameCommand(eventWaiter),
                            new ClearingCommand(eventWaiter),
                            new GiveRolesCommand(eventWaiter),
                            new TriggerSwitchForAnti(eventWaiter),
                            new RuleInfoCommand(eventWaiter),
                            new BotPingCommand(eventWaiter),
                            new HelpCommand()
                    )
                    .useHelpBuilder(false)
                    .build();
    }

    private void buildJDA() throws Exception{
        LOGGER.info("Starting buildJDA");
        jda = JDABuilder.createDefault(Config.getToken())
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(eventWaiter, buildCommandClient())
                .build();
        jda.awaitReady();
    }

    public static JDA getJDA(){
        Optional<JDA> jdaOrNull = Optional.ofNullable(jda);
        return jdaOrNull.orElseThrow(() -> new NoSuchElementException("JDA is null! Run `buildJDA()` first!"));
    }
}
