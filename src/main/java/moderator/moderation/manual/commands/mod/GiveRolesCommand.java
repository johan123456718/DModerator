package moderator.moderation.manual.commands.mod;

import com.jagrosh.jdautilities.command.CommandEvent;
import moderator.config.Config;
import moderator.moderation.manual.commands.CustomCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.awt.Color;

import static moderator.moderation.utils.embedtemplates.CustomEmbedMsg.DefaultMessages;

public class GiveRolesCommand extends CustomCommand {

    public GiveRolesCommand(){
        super.name = "giverole";
        super.help = "Gives user a role";
        super.aliases = new String[]{"gr", "GR", "giveRole", "GiveRole", "Giverole"};
        super.category = Config.getModCategory();
        super.cooldown = Config.getDefaultCooldown();
        super.arguments = "@Role";
    }

    @Override
    protected void execute(CommandEvent event) {
        super.customCommandExecute(event);
    }

    @Override
    protected void handleArgs() {
        try{
            validateArgs();
            businessLogic();
        } catch(IllegalArgumentException | HierarchyException e){
            customEmbed.sendErrorEmbed(e.getMessage(), true);
        } catch(Exception e){
            customEmbed.sendErrorEmbed(DefaultMessages.ERROR, true);
        }
    }

    @Override
    protected void validateArgs() {
        if(super.msgUtils.noUserMentioned()) throw new IllegalArgumentException(DefaultMessages.NO_USER_ERROR);
        if(super.msgUtils.noRoleMentioned()) throw new IllegalArgumentException(DefaultMessages.NO_ROLES_ERROR);
    }

    @Override
    protected void businessLogic() {
        Member targetMember = super.msgUtils.getFirstMentionedMember();
        Role givenRole = super.msgUtils.getFirstMentionedRole();
        String successMessage = "Role " + givenRole.getName() + " given to " + targetMember.getEffectiveName() + "!";

        giveRoleToUser(targetMember, givenRole);

        super.customEmbed.sendSuccessEmbed(successMessage, true);
    }

    private void giveRoleToUser(Member targetUser, Role givenRole){
        Guild currentServer = msgUtils.getCurrentEvent().getGuild();
        try{
            currentServer.addRoleToMember(targetUser, givenRole).queue();
        } catch(HierarchyException e){
            throw new HierarchyException(DefaultMessages.INSUFFICIENT_PERMS_ERROR);
        }
    }
}
