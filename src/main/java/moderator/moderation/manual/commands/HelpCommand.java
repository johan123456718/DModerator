package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

import java.awt.*;

public class HelpCommand extends Command {


    public HelpCommand(){
        super.name = "commands";
        super.help = "Displays the commands this bot can perform";
        super.aliases = new String[]{"command", "Commands", "Command", "cmd", "cmds", "c"};
        super.cooldown = 10;
    }

    @Override
    protected void execute(CommandEvent event) {
        List<Command> commands = event.getClient().getCommands();
        Color color = event.getGuild().getSelfMember().getColor();

        event.reply(createEmbed(commands, color).build());
    }

    private EmbedBuilder createEmbed(List<Command> commands, Color color){
        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(color).setTitle("Bot commands");

        for(Command command: commands){
            embedBuilder.addField(createField(command));
        }

        return embedBuilder;
    }

    private MessageEmbed.Field createField(Command command){
        return new MessageEmbed.Field(command.getName(), command.getHelp(), false);
    }

}
