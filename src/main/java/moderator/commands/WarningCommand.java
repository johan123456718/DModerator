package moderator.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.Main;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class WarningCommand extends Command {

    private final EventWaiter waiter;

    public WarningCommand(EventWaiter waiter){
        this.waiter = waiter;
        this.name = "warn";
        this.aliases = new String[]{"w", "W"};
        this.help = "Will warn the user and waits for a response";
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Hello. Who do you wish to warn?");

        waiter.waitForEvent(MessageReceivedEvent.class,
                // make sure it's by the same user, and in the same channel, and for safety, a different message
                e -> e.getAuthor().equals(event.getAuthor())
                        && e.getChannel().equals(event.getChannel())
                        && !e.getMessage().equals(event.getMessage()),
                // respond, inserting the name they listed into the response
                e -> event.reply("Warned " + e.getMessage().getMentionedUsers().get(0).getAsTag() + "!\n I'm "
                        +e.getJDA().getSelfUser().getName()
                      + " and you got warned" ),
                // if the user takes more than a minute, time out
                1, TimeUnit.MINUTES, () -> event.reply("Sorry, you took too long."));
    }

    /*@Override
    public void onMessageReceived(MessageReceivedEvent event){
        MessageChannel channel = event.getChannel();
        User admin = event.getAuthor();
        String message = event.getMessage().getContentDisplay();
        boolean doesUserExist = event.getMessage().getMentionedUsers().size() > 0;
        User getMentionUser = event.getMessage().getMentionedUsers().get(0);

        if(message.startsWith(Main.prefix + "warn " + "@" + getMentionUser.getName()) && doesUserExist){
            channel.sendMessage("DON'T WARN HIM AGAIN").queue();
        }
    }*/

}
