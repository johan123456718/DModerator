package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;



public class BotPingCommand extends Command {
    private final static Logger LOGGER = LogManager.getLogger(BotPingCommand.class);
    private final EventWaiter waiter;

    public BotPingCommand(EventWaiter waiter){
        super.name = "botping";
        super.help = "Will give the bot's ping";
        super.aliases = new String[]{"bp", "BP"};
        super.category = new Category("Members");
        super.cooldown = 3;
        super.requiredRole = "Mods";
        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event) {
        LOGGER.traceEntry();
        event.reply("Ping: ... ", m -> {
            long ping = event.getMessage().getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS);
            m.editMessage("Ping: " + ping  + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms").queue();
        });
        LOGGER.traceExit();
    }
}
