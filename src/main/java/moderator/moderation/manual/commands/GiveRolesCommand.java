package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.config.Config;
import moderator.moderation.utils.MsgValidator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class GiveRolesCommand extends Command {
    private final static Logger LOGGER = LogManager.getLogger(GiveRolesCommand.class);
    private final EventWaiter waiter;
    private MsgValidator validator;

    public GiveRolesCommand(EventWaiter waiter){
        super.name = "giverole";
        super.help = "Gives a user a role";
        super.aliases = new String[]{"gr", "GR"};
        super.category = new Command.Category("Members");
        super.cooldown = 10;
        super.arguments = "[@RoleName]";
        super.requiredRole = "Mods";
        this.waiter = waiter;
        validator = new MsgValidator();
    }

    @Override
    protected void execute(CommandEvent event) {
        validator.setEvent(event);
        if(validator.msgMentionsUsers()){
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(Color.red);
            error.setTitle("⚠️You have to mention a role⚠️");
            error.setDescription("Usage: " + Config.getPrefix()
                    + "nm " + "@username ");
            event.getChannel().sendMessage(error.build()).queue();
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
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(Color.red);
                    error.setTitle("⚠️You're not allowed to put nickname⚠️");
                    error.setDescription(mentionUser.getNickname() + " have a higher role or equal role to yours");
                    error.setImage("https://media.giphy.com/media/6Q2KA5ly49368/giphy.gif");
                    event.getChannel().sendMessage(error.build()).queue();
                }
            }catch(IndexOutOfBoundsException e){
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(Color.red);
                error.setTitle("⚠️You didn't insert a role⚠️");
                error.setDescription("Usage: @role");
                error.setImage("https://media.giphy.com/media/l4FGuhL4U2WyjdkaY/giphy.gif");
                event.getChannel().sendMessage(error.build()).queue();
            }
        }
    }
}
