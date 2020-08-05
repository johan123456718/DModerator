package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;


public class AdjustNickNameCommand extends Command {
    private final static Logger LOGGER = LogManager.getLogger(AdjustNickNameCommand.class);
    private final EventWaiter waiter;
    //private final Permission[] requiredRoles = {Permission.MANAGE_ROLES, Permission.ADMINISTRATOR };

    public AdjustNickNameCommand(EventWaiter waiter){
        super.name = "nickname";
        super.help = "Gives a user a nickname";
        super.aliases = new String[]{"nm"};
        super.category = new Command.Category("Members");
        super.cooldown = 10;
        super.arguments = "[nickname]";
        super.requiredRole = "Mods";
        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if(event.getMessage().getMentionedUsers().size() == 0){
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(Color.red);
            error.setTitle("⚠️You have to mention a user⚠️");
            error.setDescription("Usage: " + Config.getPrefix()
                    + "nm " + "@username " + "[nickname]");
            event.getChannel().sendMessage(error.build()).queue();
        }else if(args.length == 2){
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(Color.red);
            error.setTitle("⚠️You have to mention a nickname⚠️");
            error.setDescription("Usage: " + Config.getPrefix()
                    + "nm " + "@username " + "[nickname]");
            event.getChannel().sendMessage(error.build()).queue();
        }else {
            try {
                Member mentionUser = event.getMessage().getMentionedMembers().get(0);
                insertNickName(event, mentionUser, args[2]);
            } catch (IndexOutOfBoundsException ex) {
                LOGGER.error(ex.getMessage());
                event.reply("You need to provide the name as a mention.");
            }
        }
    }

    private void insertNickName(CommandEvent event, Member mentionUser, String nickName){
        if (mentionUser != null && event.getAuthor().equals(event.getAuthor()) && event.getChannel().equals(event.getChannel())) {
            try {
                mentionUser.modifyNickname(nickName).queue();
                event.reply("Change successful ");
            }catch(HierarchyException e){
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(Color.red);
                error.setTitle("⚠️You're not allowed to put nickname⚠️");
                error.setDescription(mentionUser.getNickname() + " have a higher role or equal role to yours");
                error.setImage("https://media.giphy.com/media/6Q2KA5ly49368/giphy.gif");
                event.getChannel().sendMessage(error.build()).queue();
            }
        }
    }
}
