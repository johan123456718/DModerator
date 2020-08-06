package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.config.Config;
import moderator.moderation.embed.EmbedTemplate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;

public class ClearingCommand extends Command {
    private final EventWaiter waiter;
    public ClearingCommand(EventWaiter waiter){
        super.name = "clear";
        super.help = "Clearing the chat with x messages";
        super.aliases = new String[]{"clr"};
        super.category = new Command.Category("Members");
        super.cooldown = 1;
        super.arguments = "[nrOfMessages]";
        this.requiredRole = "Mods";
        this.waiter = waiter;
    }


    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if(args.length <= 1){
            String title = "️Specifiy your command please";
            String description = "Usage: " + Config.getPrefix() + "clear " +
                    "nrOfMessages [min = 2] [max = 101]";
            MessageEmbed createError = EmbedTemplate.getDefaultError(title, description);
            event.getChannel().sendMessage(createError).queue();
        }else {
            try {
                event.getMessage().delete().queue();
                TextChannel target = event.getMessage().getTextChannel();
                purgeMessages(event, target, Integer.parseInt(args[1]));
                String title = "Successfully deleted " + args[1] + ".";
                MessageEmbed createSuccess = EmbedTemplate.getDefaultSuccess(title);
                event.getChannel().sendMessage(createSuccess).queue();
            }catch(IllegalArgumentException e){
                String title = "WRONG ARGUMENT";
                String description = "Usage: " + Config.getPrefix()
                        + "clear " + "nrOfMessages, [min = 2] [max = 101]]";
                MessageEmbed createError = EmbedTemplate.getDefaultError(title, description);
                event.getChannel().sendMessage(createError).queue();
            }
        }
    }

    private void purgeMessages(CommandEvent event, TextChannel channel, int nrOfMessages){
        List<Message> message = event.getChannel().getHistory().
                retrievePast(nrOfMessages).complete();
        channel.deleteMessages(message).queue();
    }
}
