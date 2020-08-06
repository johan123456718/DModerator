package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.moderation.auto.filter.AntiSpamFilter;
import moderator.moderation.utils.MsgValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TriggerSwitchForAnti extends Command {
    private final EventWaiter waiter;
    private List<Object> registeredListeners;
    private final static Logger LOGGER = LogManager.getLogger(TriggerSwitchForAnti.class);
    private boolean isOn;
    MsgValidator validator = new MsgValidator();

    public TriggerSwitchForAnti(EventWaiter waiter){
        super.name = "anti";
        super.help = "Triggers anti spam";
        super.aliases = new String[]{"a", "A"};
        super.category = new Command.Category("Admin");
        super.cooldown = 3;
        super.arguments = "[on, off]";
        super.requiredRole = "Admin";
        this.waiter = waiter;
        isOn = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        validator.setEvent(event);

        if(validator.containsArg("on") && !isOn){
            event.reply("Anti curse trigger: on");
            event.getJDA().addEventListener(new AntiSpamFilter());
            registeredListeners = event.getJDA().getRegisteredListeners();
            isOn = true;
        }
        if (validator.containsArg("off") && isOn){
            event.reply("Anti curse trigger: off");
            event.getJDA().removeEventListener(registeredListeners.get(2));
            isOn = false;
        }
    }

}
