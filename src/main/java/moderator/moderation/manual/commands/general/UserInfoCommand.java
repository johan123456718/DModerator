package moderator.moderation.manual.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.config.Config;
import moderator.moderation.utils.MsgUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class UserInfoCommand extends Command {

    private MsgUtils validator;
    private final EventWaiter waiter;

    public UserInfoCommand(EventWaiter waiter){
        super.name = "user";
        super.help = "Get some information about a user";
        super.aliases = new String[]{"u","U", "userinfo", "userInfo", "UserInfo"};
        super.category = Config.getGeneralCategory();
        super.cooldown = 10;
        super.arguments = "@Member";
        super.requiredRole = "Programming Friends";
        this.waiter = waiter;
        validator = new MsgUtils();
    }

    @Override
    protected void execute(CommandEvent event) {
        validator.initMsgUtils(event);
        if(validator.noUserMentioned()){
            event.reply("Please mention the user with the command. Try again!");
        } else {
            Member mentionedUser = event.getMessage().getMentionedMembers().get(0);
            event.reply(userInfoEmbed(mentionedUser).build());
            event.reply(event.getAuthor().getAsMention() + " there you go");
        }
    }

    private EmbedBuilder userInfoEmbed(Member member){
        Color embedColor = member.getRoles().get(0).getColor();
        String avatarUrl = member.getUser().getAvatarUrl();
        String desc = "Joined: " + member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd-MM-YYYY"));
        String user = member.getEffectiveName();
        String userTag = member.getUser().getAsTag();
        String status = member.getOnlineStatus().name().replaceAll("_", " ");// modifies DO-NOT-DISTURBED
        String roles = member.getRoles()
                .stream()
                .map(n -> String.valueOf(n.getAsMention()))
                .collect(Collectors.joining(" "));


        return new EmbedBuilder()
                .setColor(embedColor)
                .setThumbnail(avatarUrl)
                .setDescription(desc)
                .setAuthor(userTag, null, avatarUrl)
                .addField("Nickname: ", user, true)
                .addField("Status: ", status, true)
                .addField("Roles: ", roles, false)
                .setFooter("End of information", null)
                .setImage("https://media.giphy.com/media/o0vwzuFwCGAFO/giphy.gif");
    }

}
