package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.Permission;

import java.time.temporal.ChronoUnit;

public class BotPingCommand extends Command {
    private final EventWaiter waiter;
    private final Permission[] requiredRoles = {Permission.MANAGE_ROLES, Permission.ADMINISTRATOR };
    public BotPingCommand(EventWaiter waiter){
        super.name = "botping";
        super.help = "Will give the bot's ping";
        super.aliases = new String[]{"bp", "BP"};
        super.category = new Category("Members");
        super.cooldown = 3;
        super.userPermissions = requiredRoles;
        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Ping: ... ", m -> {
            long ping = event.getMessage().getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS);
            m.editMessage("Ping: " + ping  + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms").queue();
        });
    }
}
