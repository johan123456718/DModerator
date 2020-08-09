package moderator.moderation.manual.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.config.Config;
import moderator.moderation.manual.commands.CustomCommand;
import moderator.moderation.utils.MsgUtils;
import moderator.moderation.utils.embedtemplates.ImgUrlRandomizer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static moderator.moderation.utils.embedtemplates.CustomEmbedMsg.DefaultMessages;
import static moderator.moderation.utils.embedtemplates.ImgUrlRandomizer.ImageFile;

public class UserInfoCommand extends CustomCommand {

    private ImgUrlRandomizer successImg;

    public UserInfoCommand(){
        super.name = "user";
        super.help = "Display user information";
        super.aliases = new String[]{"u","U", "userinfo", "userInfo", "UserInfo"};
        super.category = Config.getGeneralCategory();
        super.cooldown = Config.getDefaultCooldown();
        super.arguments = "@Member";
        successImg = new ImgUrlRandomizer();
    }

    @Override
    protected void execute(CommandEvent event) {
        super.customCommandExecute(event);
    }

    @Override
    protected void validateArgs(){
        if(super.msgUtils.noUserMentioned()) throw new IllegalArgumentException(DefaultMessages.NO_USER_ERROR);
    }

    @Override
    protected void businessLogic(){
        Member targetMember = super.msgUtils.getFirstMentionedMember();
        EmbedBuilder userInfoEmbed = userInfoEmbed(targetMember);
        sendUserInfoEmbed(userInfoEmbed);
    }


    private void sendUserInfoEmbed(EmbedBuilder userInfoEmbed){
        msgUtils.getCurrentEvent().reply(userInfoEmbed.build());
    }

    private EmbedBuilder userInfoEmbed(Member member){
        Color embedColor = member.getRoles().get(0).getColor();
        String avatarUrl = member.getUser().getAvatarUrl();
        String userName  = member.getEffectiveName();
        String userTag   = member.getUser().getAsTag();
        String status    = getMemberOnlineStatus(member);
        String roles     = allRolesAsString(member);
        String desc      = getMemberJoinDate(member);

        return new EmbedBuilder()
                .setColor(embedColor)
                .setThumbnail(avatarUrl)
                .setDescription(desc)
                .setAuthor(userTag, null, avatarUrl)
                .addField("Nickname: ", userName, true)
                .addField("Status: ", status, true)
                .addField("Roles: ", roles, false)
                .setImage(successImg.getRandomImgURL(ImageFile.SUCCESS_URL))
                .setFooter("That it folks!", "https://cdn.discordapp.com/emojis/725006895395635210.png?v=1");
    }

    private String getMemberJoinDate(Member targetMember){
        return "Joined: " + targetMember.getTimeJoined().format(DateTimeFormatter.ofPattern("dd-MM-YYYY"));
    }

    private String getMemberOnlineStatus(Member targetMember){
        return targetMember.getOnlineStatus().name().replaceAll("_", " ");// modifies DO-NOT-DISTURBED status
    }

    private String allRolesAsString(Member targetMember){
        return targetMember.getRoles()
                .stream()
                .map(n -> String.valueOf(n.getAsMention()))
                .collect(Collectors.joining(" "));
    }

}
