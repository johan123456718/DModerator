package moderator.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class AdjustNickNameCommand extends Command {
    private final EventWaiter waiter;

    public AdjustNickNameCommand(EventWaiter waiter){
        super.name = "nickname";
        super.help = "Gives a user a nickname";
        super.aliases = new String[]{"nm"};
        super.category = new Command.Category("Members");
        super.cooldown = 10;
        super.arguments = "[nickname]";
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
                    event.reply("Ok! Now we got " + mentionUser.getUser().getName() + ". \nWhat do you want to give the person for nickname?");
                    insertNickName(event, mentionUser);

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
                        mentionUser.modifyNickname(nickName).queue();
                        event.reply("Change successful ");
                    }
                });
    }
}
