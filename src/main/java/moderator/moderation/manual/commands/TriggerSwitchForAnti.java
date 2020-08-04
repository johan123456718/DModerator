package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.moderation.auto.filter.AntiSpamFilter;
import net.dv8tion.jda.api.Permission;

public class TriggerSwitchForAnti extends Command {
    private final EventWaiter waiter;
    private final Permission[] requiredRoles = {Permission.MANAGE_ROLES, Permission.ADMINISTRATOR };

    public TriggerSwitchForAnti(EventWaiter waiter){
        super.name = "anti";
        super.help = "Triggers anti spam";
        super.aliases = new String[]{"a", "A"};
        super.category = new Command.Category("Admin");
        super.cooldown = 10;
        super.arguments = "[on, off]";
        super.userPermissions = requiredRoles;
        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if(args[1].equals("on")){
            event.reply("Anti curse trigger: on");
            event.getJDA().addEventListener(new AntiSpamFilter());
        }
        if (args[1].equals("off")){
            event.reply("Anti curse trigger: off");
            event.getJDA().removeEventListener();
        }
    }
}
