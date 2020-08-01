package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;

public class ModeratorInfoCommand extends Command {
    private final EventWaiter waiter;
    private final Permission[] requiredRoles = {Permission.MANAGE_ROLES, Permission.ADMINISTRATOR };
    public ModeratorInfoCommand(EventWaiter waiter){
        super.name = "moderator";
        super.help = "This will show you the moderators abilities";
        super.aliases = new String[]{"mr"};
        super.category = new Command.Category("Members");
        super.cooldown = 10;
        super.userPermissions = requiredRoles;
        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Here's the rules");
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.cyan)
                .setTitle("Moderator abilities")
                .addField("1. Delete message from user", "You can only do this as a moderator❗️", false)
                .addField("2. Use some commands including warn" , "See #x", false)
                .addField("3. Assign roles" , "See #y", false)
                .addField("4. Edit nicknames" , "Have some fun 🤣", false);

        event.reply(eb.build());
        event.reply(event.getAuthor().getAsMention() + " there you go");
    }
}
