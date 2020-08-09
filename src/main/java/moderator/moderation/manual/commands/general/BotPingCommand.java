package moderator.moderation.manual.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import moderator.config.Config;
import java.time.temporal.ChronoUnit;



public class BotPingCommand extends Command {

    public BotPingCommand(){
        super.name = "botping";
        super.help = "Use to test if bot works and receive ping info";
        super.aliases = new String[]{"bp", "BP", "ping", "Ping", "BotPing", "Botping"};
        super.cooldown = Config.getDefaultCooldown();
        super.category = Config.getGeneralCategory();
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Ping: ... ", m -> {
            long ping = event.getMessage().getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS);
            m.editMessage("Ping: " + ping  + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms").queue();
        });
    }
}
