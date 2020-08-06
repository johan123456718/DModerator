package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.config.Config;
import moderator.moderation.utils.EmbedMessageTemplate;
import moderator.moderation.utils.MsgValidator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Stream;


public class AdjustNickNameCommand extends Command {
    //private final Permission[] requiredRoles = {Permission.MANAGE_ROLES, Permission.ADMINISTRATOR };
    private final MsgValidator validator;

    public AdjustNickNameCommand(EventWaiter waiter){
        super.name = "nickname";
        super.help = "Gives a user a nickname";
        super.aliases = new String[]{"nm", "nick", "Nick", "Nickname"};
        super.category = new Command.Category("Members");
        super.cooldown = 10;
        super.arguments = "[nickname]";
        super.requiredRole = "Mods";
        validator = new MsgValidator();
    }

    @Override
    protected void execute(CommandEvent event) {
        validator.setEvent(event);

        if(validator.noUserMentioned())
            sendErrorEmbed(event, "⚠ Please mention a user!");
        else if(validator.splitMsgArgs().length < 2)
            sendErrorEmbed(event, "⚠ Please include nickname!");
        else {
            Member member = event.getMessage().getMentionedMembers().get(0);
            String[] msgArgs = validator.splitMsgArgs();
            String nickname = msgArgs[msgArgs.length-1];

            insertNickName(event, member, nickname);
        }
    }

    private void insertNickName(CommandEvent event, Member member, String nickName){
        try {
            member.modifyNickname(nickName).queue();
            event.reply("Change successful ");
        }catch(HierarchyException e){
            String error = "⚠ You lack the permissions to give that person a nickname";
            String url = "https://media.giphy.com/media/6Q2KA5ly49368/giphy.gif";

            sendErrorEmbed(event, error, url);
        }
    }

    private void sendErrorEmbed(CommandEvent event, String error){
        event.getChannel().sendMessage(createErrorEmbed(error).build()).queue();
    }

    private void sendErrorEmbed(CommandEvent event, String error, String url){
        EmbedBuilder embed = createErrorEmbed(error).setImage(url);

        event.getChannel().sendMessage(createErrorEmbed(error).build()).queue();
    }

    private EmbedBuilder createErrorEmbed(String error){
        String help = "Correct command format:  `" + Config.getPrefix() + "nm " + "@user [new nickname]`";
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle(error)
                .setDescription(help);
    }

    private String extractUserFromMsg(CommandEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");
        String mentionedUserStartsWith = "<@";

        return Stream.of(args).filter(arg -> arg.startsWith(mentionedUserStartsWith)).findFirst().orElse("no user");
    }
}
