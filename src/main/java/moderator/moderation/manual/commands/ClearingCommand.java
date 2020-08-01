package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;

public class ClearingCommand extends Command {
    private final EventWaiter waiter;
    private final Permission[] requiredRoles = {Permission.MANAGE_ROLES, Permission.ADMINISTRATOR };
    public ClearingCommand(EventWaiter waiter){
        super.name = "clear";
        super.help = "Clearing the chat with x messages";
        super.aliases = new String[]{"clr"};
        super.category = new Command.Category("Members");
        super.cooldown = 1;
        super.arguments = "[nrOfMessages]";
        this.userPermissions = requiredRoles;
        this.waiter = waiter;
    }


    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if(args.length <= 2){
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(Color.red);
            error.setTitle("⚠️Specifiy your command please⚠️");
            error.setDescription("Usage: " + Config.getPrefix()
                    + "clear " + "#channel " + "[nrOfMessages [min = 2] [max = 101]]");
            event.getChannel().sendMessage(error.build()).queue();
        }else {
            try {
                event.getMessage().delete().queue();
                TextChannel target = event.getMessage().getMentionedChannels().get(0);
                purgeMessages(event, target, Integer.parseInt(args[2]));
                EmbedBuilder success = new EmbedBuilder();
                success.setColor(Color.green);
                success.setTitle("✅ Successfully deleted " + args[2] + ".");
                event.getChannel().sendMessage(success.build()).queue();
            }catch(IllegalArgumentException e){
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(Color.red);
                error.setTitle("⚠️WRONG ARGUMENT⚠️");
                error.setDescription("Usage: " + Config.getPrefix()
                        + "clear " + "#channel " + "[nrOfMessages, [min = 2] [max = 101]]");
                event.getChannel().sendMessage(error.build()).queue();
            }
        }
    }

    private void purgeMessages(CommandEvent event, TextChannel channel, int nrOfMessages){
        List<Message> message = event.getChannel().getHistory().
                retrievePast(nrOfMessages).complete();
        channel.deleteMessages(message).queue();
    }
}
