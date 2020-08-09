package moderator.moderation.manual.commands.mod;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.config.BotConfig;
import moderator.config.Config;
import moderator.moderation.auto.filter.ExpletivesFilter;
import moderator.moderation.manual.commands.CustomCommand;

import java.util.List;

import static moderator.moderation.utils.embedtemplates.CustomEmbedMsg.DefaultMessages;

public class ExpletivesFilterSwitch extends CustomCommand {

    private final String INCORRECT_ARG = "Must specify 'on' or 'off'! Try again!";
    private final String SWITCH_ON = "on";
    private final String SWITCH_OFF = "off";
    private ExpletivesFilter expletivesFilterPointer;
    private boolean isOn;
    private boolean isOff;

    public ExpletivesFilterSwitch(){
        super.name = "filter";
        super.help = "Turns on/off the expletives filter ";
        super.aliases = new String[]{"F", "f", "Filter"};
        super.category = Config.getModCategory();
        super.cooldown = Config.getDefaultCooldown();
        super.arguments = "[on, off]";
        isOn = false;
        isOff = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        super.customCommandExecute(event);
    }

    @Override
    protected void validateArgs() {
        boolean containsCorrectArgs = super.msgUtils.containsTargetArg(SWITCH_ON) || super.msgUtils.containsTargetArg(SWITCH_OFF);

        if(!containsCorrectArgs) throw new IllegalArgumentException(INCORRECT_ARG);
        if(super.msgUtils.hasArgsCount(0)) throw new IllegalArgumentException(DefaultMessages.MISSING_ARGS_ERROR);
    }

    @Override
    protected void businessLogic() {
        runFilter();
        String filterStatus = "Expletive filter is " + ((isOn) ? "on":"off") + "!";

        super.customEmbed.sendSuccessEmbed(filterStatus, true);
    }

    private void runFilter(){
        if(super.msgUtils.containsTargetArg(SWITCH_ON) && isOff){
            saveExpletivesFilter();
            toggleFilter();
        } else if(super.msgUtils.containsTargetArg(SWITCH_OFF) && isOn){
            removeExpletivesFilter();
            toggleFilter();
        } else {
            throw new IllegalCallerException(DefaultMessages.ERROR);
        }
    }

    private void saveExpletivesFilter(){
        BotConfig.getJDA().addEventListener(new ExpletivesFilter());
        List<Object> currentListeners = BotConfig.getJDA().getRegisteredListeners();
        int indexOfExpletivesFilter = currentListeners.size() - 1;

        expletivesFilterPointer = (ExpletivesFilter) currentListeners.get(indexOfExpletivesFilter);
    }

    private void removeExpletivesFilter(){
        BotConfig.getJDA().removeEventListener(expletivesFilterPointer);
        expletivesFilterPointer = null;
    }

    private void toggleFilter(){
        isOn = !isOn;
        isOff = !isOff;
    }
}
