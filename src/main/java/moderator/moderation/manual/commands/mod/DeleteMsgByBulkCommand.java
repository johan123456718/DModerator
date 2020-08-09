package moderator.moderation.manual.commands.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import moderator.config.Config;
import moderator.moderation.manual.commands.CustomCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

import static moderator.moderation.utils.embedtemplates.CustomEmbedMsg.DefaultMessages;


import java.util.List;

public class DeleteMsgByBulkCommand extends CustomCommand {
    private final int MIN_ARG = 2;
    private final int MAX_ARG = 101;
    private final String ILLEGAL_ARGS = "Number must be in range of 2 to 101!";
    private final String NO_NUMBER_FOUND = "Must enter a valid number!";
    private final String NO_ARG_FOUND = "Did not specify amount of messages to clear!";
    private final String SUCCESS_DELETE = "Successfully Deleted: ";

    public DeleteMsgByBulkCommand(){
        super.name = "clear";
        super.help = "Deletes # of messages from current channel";
        super.aliases = new String[]{"clr"};
        super.category = Config.getModCategory();
        super.cooldown = 5;
        super.arguments = "msgNumber";
        this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
    }

    @Override
    protected void execute(CommandEvent event) {
        super.customCommandExecute(event);
    }

    @Override
    protected void validateArgs(){
        if(super.msgUtils.hasArgsCount(0)) throw new IllegalArgumentException(NO_ARG_FOUND);

        try{
            int arg = Integer.parseInt(msgUtils.getFirstArgAfterMentions());
            boolean numberNotValid = arg < MIN_ARG || arg > MAX_ARG;

            if(numberNotValid) throw new IllegalArgumentException(ILLEGAL_ARGS);
        } catch (NumberFormatException e){
            throw new NumberFormatException(NO_NUMBER_FOUND);
        }
    }

    @Override
    protected void businessLogic(){
        int numOfMessages = Integer.parseInt(msgUtils.getFirstArgAfterMentions());
        deleteChannelMessages(numOfMessages);

        customEmbed.sendSuccessEmbed(SUCCESS_DELETE + numOfMessages, true);
    }

    private void deleteChannelMessages(int numOfMessages){
        CommandEvent event = msgUtils.getCurrentEvent();
        List<Message> messages = event.getChannel().getHistory().retrievePast(numOfMessages).complete();

        event.getMessage().getTextChannel().deleteMessages(messages).queue();
    }

}
