package moderator.moderation.utils.embedtemplates;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;

import static moderator.moderation.utils.embedtemplates.ImgUrlRandomizer.ImageFile;

public class CustomEmbedMsg implements Cloneable {

    public static class DefaultMessages {
        public final static String ERROR = "Something went wrong, try again!";
        public final static String NO_USER_ERROR = "No user mentioned! Try again!";
        public final static String NO_ROLES_ERROR = "No role mentioned! Try again!";
        public final static String MISSING_ARGS_ERROR = "Insufficient arguments provided! Try again!";
        public final static String INSUFFICIENT_PERMS_ERROR = "Insufficient Permissions! Command can't be executed!";
        public final static String SUCCESS = "Success!";
    }

    private CommandEvent event;
    private String cmdHelp;
    private ImgUrlRandomizer img;

    public CustomEmbedMsg() {
        img = new ImgUrlRandomizer();
    }

    public void initCustomEmbed(CommandEvent event, String cmdHelp){
        this.event = event;
        this.cmdHelp = cmdHelp;
    }

    private CommandEvent getEvent(){
        if(event == null)  throw new IllegalStateException("Event not set!");
        return event;
    }

    public EmbedBuilder errorEmbed(String error, boolean includeImage){
        String imgURL = includeImage ? img.getRandomImgURL(ImageFile.ERROR_URL) : "";
        String emoji = "⚠️";
        String title = emoji + " " +  error + " " + emoji;

        return new EmbedBuilder().setTitle(title)
                                 .setDescription(cmdHelp)
                                 .setColor(Color.RED)
                                 .setImage(imgURL);
    }

    public EmbedBuilder successEmbed(String success, boolean includeImage){
        String imgURL = includeImage ? img.getRandomImgURL(ImageFile.SUCCESS_URL) : "";
        String emoji = "\uD83C\uDF89️"; // congrats symbol
        String title = emoji + " " +  success + " " + emoji;
        String desc = "Command successfully executed!";

        return new EmbedBuilder().setTitle(title)
                                 .setDescription(desc)
                                 .setColor(Color.GREEN)
                                 .setImage(imgURL);
    }

    public void sendErrorEmbed(String error, boolean includeImage){
        event.getChannel().sendMessage(errorEmbed(error, includeImage).build()).queue();
    }

    public void sendSuccessEmbed(String success, boolean includeImage){
        event.getChannel().sendMessage(successEmbed(success, includeImage).build()).queue();
    }

}
