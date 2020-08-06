package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.config.Config;
import moderator.moderation.utils.MsgValidator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.util.stream.Stream;


public class AdjustNickNameCommand extends Command {
    private final static Logger LOGGER = LogManager.getLogger(AdjustNickNameCommand.class);
    private final EventWaiter waiter;
    //private final Permission[] requiredRoles = {Permission.MANAGE_ROLES, Permission.ADMINISTRATOR };
    private MsgValidator validator;

    public AdjustNickNameCommand(EventWaiter waiter){
        super.name = "nickname";
        super.help = "Gives a user a nickname";
        super.aliases = new String[]{"nm", "nick", "Nick", "Nickname"};
        super.category = new Command.Category("Members");
        super.cooldown = 10;
        super.arguments = "[nickname]";
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
            error.setTitle("⚠️You have to mention a user⚠️");
            error.setDescription("Usage: " + Config.getPrefix()
                    + "nm " + "@username " + "[nickname]");
            event.getChannel().sendMessage(error.build()).queue();
        }else if(!validator.containsArg(theMentionedUserTrueName(event))){

            EmbedBuilder error = new EmbedBuilder();
            error.setColor(Color.red);
            error.setTitle("⚠️You have to mention a nickname⚠️");
            error.setDescription("Usage: " + Config.getPrefix()
                    + "nm " + "@username " + "[nickname]");
            event.getChannel().sendMessage(error.build()).queue();
        }else {
            try {
                Member member = event.getMessage().getMentionedMembers().get(0);
                String[] args = validator.splitMsgArgs();
                String nickname = args[args.length-1];
                insertNickName(event, member, nickname);
            } catch (IndexOutOfBoundsException ex) {
                LOGGER.error(ex.getMessage());
                event.reply("You need to provide the name as a mention.");
            }
        }
    }

    private String theMentionedUserTrueName(CommandEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");
        String mentionedUserStartsWith = "<@";

        return Stream.of(args).filter(arg -> arg.startsWith(mentionedUserStartsWith)).findFirst().orElse("no user");
    }

    private void insertNickName(CommandEvent event, Member member, String nickName){
        String user = member.getEffectiveName();
        if (member != null && event.getAuthor().equals(event.getAuthor()) && event.getChannel().equals(event.getChannel())) {
            try {
                member.modifyNickname(nickName).queue();
                event.reply("Change successful ");
            }catch(HierarchyException e){
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(Color.red);
                error.setTitle("⚠️You're not allowed to put nickname⚠️");
                error.setDescription(user + " have a higher role or equal role to yours");
                error.setImage("https://media.giphy.com/media/6Q2KA5ly49368/giphy.gif");
                event.getChannel().sendMessage(error.build()).queue();
            }
        }
    }
}
