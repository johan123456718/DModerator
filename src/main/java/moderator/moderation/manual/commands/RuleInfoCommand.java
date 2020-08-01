package moderator.moderation.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class RuleInfoCommand extends Command {
    private final EventWaiter waiter;

    public RuleInfoCommand(EventWaiter waiter){
        super.name = "rule";
        super.help = "This will give you the rules";
        super.aliases = new String[]{"r"};
        super.category = new Category("Members");
        super.cooldown = 10;
        this.waiter = waiter;
    }
    @Override
    protected void execute(CommandEvent event) {
        event.reply("Here's the rules");
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.cyan)
                .setTitle("Rules of the server")
                .addField("No cursing❗", "if you use racist-, homophobic- or cursing words, you will get banned", false)
                .addField("Don't be annoying❗" , "if you are still annoying, you will get x-minutes mute", false)
                .addField("Don't interrupt people❗" , "if you interrupt people, you will get warned", false)
                .addField("Dont be a dick❗", "if you are a dick, you will get x-minutes mute", false);

        event.reply(eb.build());
        event.reply(event.getAuthor().getAsMention() + " there you go");
    }
}
