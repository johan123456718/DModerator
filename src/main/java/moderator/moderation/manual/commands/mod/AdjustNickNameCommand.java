package moderator.moderation.manual.commands.mod;

import com.jagrosh.jdautilities.command.CommandEvent;
import moderator.config.Config;
import moderator.moderation.manual.commands.CustomCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import static moderator.moderation.utils.embedtemplates.CustomEmbedMsg.DefaultMessages;


public class AdjustNickNameCommand extends CustomCommand {

    public AdjustNickNameCommand(){
        super.name = "nm";
        super.help = "Changes user's nickname";
        super.aliases = new String[]{"nickname", "nick", "Nick", "Nickname", "NM"};
        super.category = Config.getModCategory();
        super.cooldown = Config.getDefaultCooldown();
        super.arguments = "newNickname";
    }

    @Override
    protected void execute(CommandEvent event) {
        super.customCommandExecute(event);
    }

    @Override
    protected void handleArgs(){
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
    protected void validateArgs(){
        if(super.msgUtils.noUserMentioned()) throw new IllegalArgumentException(DefaultMessages.NO_USER_ERROR);
        if(!super.msgUtils.argsExistAfterMentions()) throw new IllegalArgumentException(DefaultMessages.MISSING_ARGS_ERROR);
    }

    @Override
    protected void businessLogic(){
        Member targetMember = super.msgUtils.getFirstMentionedMember();
        String newName = super.msgUtils.getFirstArgAfterMentions();
        String successMessage = targetMember.getEffectiveName() + " changed to " + newName + "!";

        insertNickName(targetMember, newName);

        super.customEmbed.sendSuccessEmbed(successMessage, true);
    }

    private void insertNickName(Member targetMember, String newName){
        try {
            targetMember.modifyNickname(newName).queue();
        } catch(HierarchyException e){
            throw new HierarchyException(DefaultMessages.INSUFFICIENT_PERMS_ERROR);
        }
    }
}
