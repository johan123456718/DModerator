package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.config.Config;
import moderator.moderation.embed.EmbedTemplate;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GiveRolesCommand extends Command {
    private final static Logger LOGGER = LogManager.getLogger(GiveRolesCommand.class);
    private final EventWaiter waiter;

    public GiveRolesCommand(EventWaiter waiter){
        super.name = "giverole";
        super.help = "Gives a user a role";
        super.aliases = new String[]{"gr", "GR"};
        super.category = new Command.Category("Members");
        super.cooldown = 10;
        super.arguments = "[@RoleName]";
        super.requiredRole = "Mods";
        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event) {
        if(event.getMessage().getMentionedUsers().size() == 0){
            String title = "You have to mention a role";
            String description = "Usage: " + Config.getPrefix()
                    + "nm " + "@username ";
            MessageEmbed errorMessage = EmbedTemplate.getDefaultError(title, description);
            event.getChannel().sendMessage(errorMessage).queue();
        }else{
            try{
                Member mentionUser = event.getMessage().getMentionedMembers().get(0);
                if (mentionUser.getNickname() != null) {
                    event.reply("Ok! Now we got " + mentionUser.getNickname());
                    insertRoleToUser(event, mentionUser);
                } else {
                    event.reply("Ok! Now we got " + mentionUser.getUser().getName());
                    insertRoleToUser(event, mentionUser);
                }
            }catch (IndexOutOfBoundsException ex) {
                LOGGER.error(ex.getMessage());
                event.reply("You need to provide the role as a mention.");
            }
        }
    }

    private void insertRoleToUser(CommandEvent event, Member mentionUser){
        Guild guild = event.getGuild();
        if (mentionUser != null && event.getAuthor().equals(event.getAuthor()) && event.getChannel().equals(event.getChannel())) {
            try {
                Role mentionRole = event.getMessage().getMentionedRoles().get(0);
                try {
                    guild.addRoleToMember(mentionUser, mentionRole).queue();
                    event.reply("Change successful ");
                } catch (HierarchyException e) {
                    String title = "You're not allowed to put nickname";
                    String description = mentionUser.getNickname() + " have a higher role or equal role to yours";
                    String url = "https://media.giphy.com/media/6Q2KA5ly49368/giphy.gif";
                    MessageEmbed errorMessage = EmbedTemplate.getErrorWithImage(title, description, url);
                    event.getChannel().sendMessage(errorMessage).queue();
                }
            }catch(IndexOutOfBoundsException e){
                String title = "You didn't insert a role";
                String description = "Usage: @role";
                String url = "https://media.giphy.com/media/l4FGuhL4U2WyjdkaY/giphy.gif";
                MessageEmbed errorMessage = EmbedTemplate.getErrorWithImage(title, description, url);
                event.getChannel().sendMessage(errorMessage).queue();
            }
        }
    }
}
