package moderator.moderation.manual.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import moderator.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Comparator;
import java.util.List;
import java.awt.Color;
import java.util.Objects;
import java.util.stream.Collectors;

public class HelpCommand extends Command {
    private final String HELP_TITLE = "All DModerator Commands:";
    private final String DESCRIPTION = "⠀"; // DON'T MODIFY; ASK RISBAH FIRST
    private final String SEPARATOR = "---------------------";
    private final boolean NO_INLINE = false;
    private Category currentCategory;


    public HelpCommand(){
        super.name = "help";
        super.help = "Displays how to use this bot";
        super.aliases = new String[]{"help", "Help", "h", "H"};
        super.cooldown = Config.getDefaultCooldown();
        super.category = Config.getGeneralCategory();
    }

    @Override
    protected void execute(CommandEvent event) {
        List<Command> botCommands = getCommandsSortedByCategory(event);
        Color userColor = event.getGuild().getSelfMember().getColor();

        event.reply(sendHelpEmbed(botCommands, userColor).build());
    }

    private EmbedBuilder sendHelpEmbed(List<Command> botCommands, Color userColor){
        EmbedBuilder helpEmbed = new EmbedBuilder().setColor(userColor).setTitle(HELP_TITLE).setDescription(DESCRIPTION);

        addFieldsToEmbed(botCommands, helpEmbed);

        return helpEmbed;
    }

    private void addFieldsToEmbed(List<Command> botCommands, EmbedBuilder helpEmbed){

        for(Command currentCommand: botCommands){
            MessageEmbed.Field field;

            if(!Objects.equals(currentCategory, currentCommand.getCategory())){
                currentCategory = currentCommand.getCategory();
                String currentCategoryName = currentCategory == null ? "No Category" : currentCategory.getName();
                field = createCategoryField(currentCategoryName);
            } else {
                field = createCommandHelpField(currentCommand);
            }

            helpEmbed.addField(field);
        }
    }

    private MessageEmbed.Field createCategoryField(String categoryName){
        String categoryDescription = "";
        String leftPadding = "---**";
        String rightPadding = "**---";
        String title = leftPadding + categoryName + rightPadding;

        return new MessageEmbed.Field(title, categoryDescription, NO_INLINE);
    }

    private MessageEmbed.Field createCommandHelpField(Command command){
        String cmdArgs = (command.getArguments() == null) ? ("") : (" " + command.getArguments());
        String cmdDescription = "*" + command.getHelp() + "*";
        String fieldTitle = "`" + Config.getPrefix() + command.getName() + cmdArgs + "`";
        String fieldValue = SEPARATOR + System.lineSeparator() + cmdDescription + System.lineSeparator();

        return new MessageEmbed.Field(fieldTitle, fieldValue, true);
    }

    private List<Command> getCommandsSortedByCategory(CommandEvent event){
        List<Command> botCommands = event.getClient().getCommands();

        return botCommands.stream()
                .sorted(Comparator.comparing(c -> c.getCategory().getName()))
                .collect(Collectors.toList());
    }

}
