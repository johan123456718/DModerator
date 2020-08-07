package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import java.util.List;
import java.awt.*;

public class HelpCommand extends Command {


    public HelpCommand(){
        super.name = "help";
        super.help = "Displays the commands this bot can perform";
        super.aliases = new String[]{"help", "Help", "h", "H"};
        super.requiredRole = "Programming Friends";
        super.cooldown = 10;
    }

    @Override
    protected void execute(CommandEvent event) {
        List<Command> commands = event.getClient().getCommands();
        Color color = event.getGuild().getSelfMember().getColor();

        event.reply(createEmbed(commands, color).build());
    }

    private EmbedBuilder createEmbed(List<Command> commands, Color color){
        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(color).setTitle("All bot commands");
        for(Command command: commands){
            embedBuilder.addField(createField(command));
        }
        embedBuilder.addBlankField(true);
        return embedBuilder;
    }

    private MessageEmbed.Field createField(Command command){
            if(command.getArguments() != null) {
                return new MessageEmbed.Field("-" + command.getName() + " " + command.getArguments(),
                        "---------------------\n" + command.getHelp() + "\nPrevilage: " + command.getRequiredRole() + "\n-------------------", true);
            }else if(command.getRequiredRole() != null){
                return new MessageEmbed.Field("-" + command.getName(),
                        "---------------------\n" + command.getHelp() + "\nPrevilage: " + command.getRequiredRole() + "\n-------------------", true);
            }
        return new MessageEmbed.Field("-" + command.getName(),
                "---------------------\n" + command.getHelp() + "\n-------------------", true);
    }
}
