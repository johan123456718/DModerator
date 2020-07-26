package moderator.config;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.commands.WarningCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.util.NoSuchElementException;
import java.util.Optional;

public class Bot {
    private static JDA jda;
    private final EventWaiter eventWaiter;

    public Bot(String fileName){
        ConfigLoad.load(fileName);
        eventWaiter = new EventWaiter();
        System.out.println("Finish loading bot configurations!");
    }

    public void run(){
        try {
            buildJDA();
            String botName =  getJDA().getSelfUser().getName();
            System.out.println(String.format("%s is now Running!",botName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // To-DO: MUST ADD :owoPolice: emoji to bot ACTIVITY!!!
    private CommandClient buildCommandClient(){
        return new CommandClientBuilder()
                    .setPrefix(ConfigLoad.prefix())
                    .setActivity(Activity.playing("Watching for any criminal behavior"))
                    .setOwnerId(ConfigLoad.owner())
                    .addCommands(
                            new WarningCommand(eventWaiter)
                    )
                    .build();

    }

    private void buildJDA() throws Exception{
        jda = JDABuilder.createDefault(ConfigLoad.token())
                .addEventListeners(eventWaiter, buildCommandClient())
                .build();
        jda.awaitReady();

        System.out.println("Finish Building JDA!");
    }

    public static JDA getJDA(){
        Optional<JDA> jdaOrNull = Optional.ofNullable(jda);
        return jdaOrNull.orElseThrow(() -> new NoSuchElementException("JDA is null! Run `buildJDA()` first!"));
    }
}
