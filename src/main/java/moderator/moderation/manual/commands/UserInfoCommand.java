package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class UserInfoCommand extends Command {

    private final EventWaiter waiter;

    public UserInfoCommand(EventWaiter waiter){
        super.name = "user";
        super.help = "Get some information about a user";
        super.aliases = new String[]{"u","U", "userinfo", "userInfo", "UserInfo"};
        super.category = new Category("Members");
        super.cooldown = 10;
        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(event.getMessage().getMentionedMembers().size()==0){
            event.reply("Please mention the user with the command. Try again!");
        } else {
            Member mentionedUser = event.getMessage().getMentionedMembers().get(0);
            event.reply(userInfoEmbed(mentionedUser).build());
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
                .addField("Roles: ", roles, false);
    }

}
