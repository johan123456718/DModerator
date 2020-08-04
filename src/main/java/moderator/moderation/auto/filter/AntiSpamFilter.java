package moderator.moderation.auto.filter;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.concurrent.*;

public class AntiSpamFilter extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        MessageChannel channel = event.getChannel();
        Member author = event.getMember();
        String msg = event.getMessage().getContentDisplay();
        Message deleteMessage = event.getMessage();
        String[] cursingWords = {"fuck", "nigger", "whore", "gay", "fag", "faggot"};
        int timesMuted = 0;
        for(int i = 0; i < cursingWords.length; i++) {
            if (msg.contains(cursingWords[i]) && event.getAuthor().equals(author.getUser())
                    && !author.getUser().isBot()) {
                if (timesMuted < 1) {
                    channel.sendMessage(author.getUser().getName() + " don't use "
                            + cursingWords[i].replace(cursingWords[i], "*")
                            + " in your sentences❗").queue();
                    channel.sendMessage("You will get 15 sec " + "mute").queue();
                    deleteMessage.delete().queue();

                    String mutedId = "738437797311938700";
                    Role mutedRole = event.getGuild().getRoleById(mutedId);

                    Guild guild = event.getGuild();
                    List<Role> allUserRoles = author.getRoles();

                    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                    ScheduledFuture scheduledFuture = scheduledExecutorService.schedule(new Callable() {
                        @Override
                        public Object call() throws Exception {
                            if (!author.getRoles().contains(mutedRole)) {
                                for (int j = 0; j < allUserRoles.size(); j++) {
                                    if (!allUserRoles.get(j).equals(mutedRole.getName())) {
                                        guild.removeRoleFromMember(author, allUserRoles.get(j)).complete();
                                    }
                                }
                                channel.sendMessage("Muted: " + author.getUser().getName() + ".").queue();
                                guild.addRoleToMember(author, mutedRole).complete();
                            }
                            return "called";
                        }
                    }, 1, TimeUnit.SECONDS);
                    scheduledExecutorService.shutdown();

                    ScheduledExecutorService scheduledExecutorService2 = Executors.newScheduledThreadPool(1);
                    ScheduledFuture scheduledFuture2 = scheduledExecutorService2.schedule(new Callable() {
                        @Override
                        public Object call() throws Exception {
                            if (!author.getRoles().contains(mutedRole)) {
                                for (int j = 0; j < allUserRoles.size(); j++) {
                                    if (!allUserRoles.get(j).equals(mutedRole.getName())) {
                                        guild.addRoleToMember(author, allUserRoles.get(j)).complete();
                                    }
                                }
                                channel.sendMessage("Unmuted: " + author.getUser().getName() + ".").queue();
                                guild.removeRoleFromMember(author, mutedRole).complete();
                            }
                            return "called2";
                        }
                    }, 15, TimeUnit.SECONDS);
                    scheduledExecutorService2.shutdown();
                    timesMuted++;
                }
            }
        }
    }
}
