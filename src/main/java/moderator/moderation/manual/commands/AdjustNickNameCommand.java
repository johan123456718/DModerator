package moderator.moderation.manual.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import moderator.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class AdjustNickNameCommand extends Command {
    private final EventWaiter waiter;
    private final Permission[] requiredRoles = {Permission.MANAGE_ROLES, Permission.ADMINISTRATOR };
    public AdjustNickNameCommand(EventWaiter waiter){
        super.name = "nickname";
        super.help = "Gives a user a nickname";
        super.aliases = new String[]{"nm"};
        super.category = new Command.Category("Members");
        super.cooldown = 10;
        super.arguments = "[nickname]";
        super.userPermissions = requiredRoles;
        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Ok! Now, give me the name of a user. Like this @name");
        waiter.waitForEvent(GuildMessageReceivedEvent.class,
        e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()),
            e -> {
                try{
                    Member mentionUser = e.getMessage().getMentionedMembers().get(0);
                    if(mentionUser.getNickname() != null) {
                        event.reply("Ok! Now we got " + mentionUser.getNickname() + ". \nWhat do you want to give the person for nickname?");
                        insertNickName(event, mentionUser);
                    }else{
                        event.reply("Ok! Now we got " + mentionUser.getUser().getName() + ". \nWhat do you want to give the person for nickname?");
                        insertNickName(event, mentionUser);
                    }
                }catch (IndexOutOfBoundsException ex) {
                    System.out.println("Exception Occured");
                    event.reply("You need to provide the name as a mention.");
                }
            }, 15, TimeUnit.SECONDS, () -> event.reply("You did not give me a nickname on time. Try again."));
    }

    private void insertNickName(CommandEvent event, Member mentionUser){
        waiter.waitForEvent(GuildMessageReceivedEvent.class,
                f -> f.getAuthor().equals(event.getAuthor()) && f.getChannel().equals(event.getChannel()),
                f -> {
                    if (mentionUser != null && f.getAuthor().equals(event.getAuthor()) && f.getChannel().equals(event.getChannel())) {
                        String nickName = f.getMessage().getContentDisplay();
                        try {
                            mentionUser.modifyNickname(nickName).queue();
                            event.reply("Change successful ");
                        }catch(HierarchyException e){
                            EmbedBuilder error = new EmbedBuilder();
                            error.setColor(Color.red);
                            error.setTitle("⚠️You're not allowed to put nickname⚠️");
                            error.setDescription(mentionUser.getNickname() + " have a higher role or equal role to yours");
                            error.setImage("https://media.giphy.com/media/6Q2KA5ly49368/giphy.gif");
                            event.getChannel().sendMessage(error.build()).queue();
                        }
                    }
                });
    }
}
