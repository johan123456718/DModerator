package moderator.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.WidgetUtil;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class UserInfoCommand extends Command {

    private final EventWaiter waiter;

    public UserInfoCommand(EventWaiter waiter){
        super.name = "user";
        super.help = "Get some information about a user";
        super.aliases = new String[]{"u"};
        super.category = new Category("Members");
        super.cooldown = 10;
        super.arguments = "[name]";
        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        event.reply("Ok! Now, give me the name of a user. Like this @name");

        waiter.waitForEvent(GuildMessageReceivedEvent.class,
            e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()),
                e -> {
                try {
                    Member name = e.getMessage().getMentionedMembers().get(0);
                    EmbedBuilder eb = new EmbedBuilder()
                            .setColor(Color.magenta)
                            .setThumbnail("http://pixelartmaker.com/art/fec737afdb065ed.png")
                            .setDescription(name.getUser().getName() + " joined on " + name.getTimeJoined().format(fmt) + " :clock: ")
                            .setAuthor("Information on " + name.getUser().getName(), "http://www.google.com", name.getUser().getAvatarUrl())
                            .addField("Status on desktop:", name.getOnlineStatus(ClientType.DESKTOP).toString(), true)
                            .addField("Nickname: ", name.getNickname() == null ? "No Nickname" : name.getNickname(), true);
                    event.reply(eb.build());
                    event.reply(event.getAuthor().getAsMention() + " there you go");
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println("Exception Occured");
                    event.reply("You need to provide the name as a mention.");
                }
            }, 15, TimeUnit.SECONDS, () -> event.reply("You did not give me a name to search. Try again.")); //Last 3 arguments specify how long to wait for the event and what to do if they never respond
    }

}
