package moderator.moderation.utils;

import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.List;
import java.util.stream.Stream;

public class MsgValidator {

    private CommandEvent event;

    public MsgValidator() {
        this.event = null;
    }

    public void setEvent(CommandEvent event){
        this.event = event;
    }

    private CommandEvent getEvent(){
        if(event == null) throw new IllegalStateException("Event not set!");
        return event;
    }

    public boolean containsArg(String target, String[] ignore){
        return Stream.of(splitMsgArgs())
                .filter(m -> !List.of(ignore).contains(m))
                .findFirst()
                .orElse("null")
                .equalsIgnoreCase(target);
    }

    public boolean containsArg(String target){
        String[] originalMessage = splitMsgArgs();
        return Stream.of(originalMessage).anyMatch(arg -> arg.equalsIgnoreCase(target));
    }

    public String[] splitMsgArgs(){
        return event.getArgs().split(" ");
    }

    public boolean msgMentionsUsers(){
        int NO_MENTIONED_MEMBERS = 0;
        return event.getMessage().getMentionedMembers().size() > NO_MENTIONED_MEMBERS;
    }

    public boolean msgHasThisManyArgs(int count){
        return event.getArgs().length() == count;
    }

}
