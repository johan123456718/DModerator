package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import moderator.config.Config;
import moderator.moderation.utils.MsgUtils;
import moderator.moderation.utils.embedtemplates.CustomEmbedMsg;

public abstract class CustomCommand extends Command {
    protected MsgUtils msgUtils;
    protected CustomEmbedMsg customEmbed;

    public CustomCommand() {
        msgUtils = new MsgUtils();
        customEmbed = new CustomEmbedMsg();
    }

    protected String getCMDHelp(){
        return "Format: `" + Config.getPrefix() + super.name + " " + getArguments() + "`";
    }

    protected final void customCommandExecute(CommandEvent event){
        msgUtils.initMsgUtils(event);
        customEmbed.initCustomEmbed(event, getCMDHelp());

        handleArgs();
    }

    protected void handleArgs() {
        try{
            validateArgs();
            businessLogic();
        } catch(IllegalArgumentException e){
            customEmbed.sendErrorEmbed(e.getMessage(), true);
        } catch(Exception e){
            customEmbed.sendErrorEmbed(CustomEmbedMsg.DefaultMessages.ERROR, true);
        }
    }

    // must override
    protected abstract void validateArgs();
    protected abstract void businessLogic();
}
