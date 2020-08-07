package moderator.moderation.auto.notifcation;
import moderator.moderation.embed.EmbedTemplate;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NotificationForUsers extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event){
        Guild guild = event.getGuild();
        TextChannel channel = guild.getTextChannelById("734060780307087470");
        String title = event.getUser().getName() + " has joined the channel";
        channel.sendMessage(EmbedTemplate.getNotificationForJoin(title)).queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event){
        Guild guild = event.getGuild();
        TextChannel channel = guild.getTextChannelById("734060780307087470");
        String title = event.getUser().getName() + " has left the channel";
        channel.sendMessage(EmbedTemplate.getNotificationForLeave(title)).queue();
    }
}
