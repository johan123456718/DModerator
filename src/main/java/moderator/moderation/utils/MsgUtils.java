package moderator.moderation.utils;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.stream.Stream;

public class MsgUtils {

    private CommandEvent event;

    public MsgUtils() {
        this.event = null;
    }

    public void initMsgUtils(CommandEvent event){
        this.event = event;
    }

    public CommandEvent getCurrentEvent(){
        if(event == null) throw new IllegalStateException("Event not set!");
        return event;
    }

    // gets all strings entered after `prefix [name]`, splitted by space
    public String[] userArgsAsArray(){
        return getCurrentEvent().getArgs().split(" ");
    }

    public Member getFirstMentionedMember(){
        return getCurrentEvent().getMessage().getMentionedMembers().get(0);
    }

    public Role getFirstMentionedRole(){
        return getCurrentEvent().getMessage().getMentionedRoles().get(0);
    }

    public boolean noUserMentioned(){
        int NO_MENTIONED_MEMBERS = 0;
        return event.getMessage().getMentionedMembers().size() == NO_MENTIONED_MEMBERS;
    }

    public boolean noRoleMentioned(){
        int NO_MENTIONED_ROLES = 0;
        return event.getMessage().getMentionedRoles().size() == NO_MENTIONED_ROLES;
    }

    // Used to check if message contains arguments after any mentions
    public boolean argsExistAfterMentions(){
        return !getFirstArgAfterMentions().equalsIgnoreCase("null");
    }

    public String getFirstArgAfterMentions(){
        String noArgs = "null";
        String ignoreMentions = "<@";

        return Stream.of(userArgsAsArray())
                .filter(m -> !m.startsWith(ignoreMentions))
                .findFirst()
                .orElse(noArgs);
    }


    public boolean containsTargetArg(String target){
        String[] originalMessage = userArgsAsArray();
        return Stream.of(originalMessage).anyMatch(arg -> arg.equalsIgnoreCase(target));
    }

    public boolean hasArgsCount(int count){
        return userArgsAsArray().length == count;
    }

}
