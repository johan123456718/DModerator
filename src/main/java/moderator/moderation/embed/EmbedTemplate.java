package moderator.moderation.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class EmbedTemplate {

    public static MessageEmbed getDefaultError(String title, String description) {
        final String warningSign = "⚠️";
        return new EmbedBuilder()
        .setColor(Color.red)
        .setTitle(warningSign + " " + title + " " + warningSign)
        .setDescription(description)
        .build();
    }

    public static MessageEmbed getErrorWithImage(String title, String description, String url){
        final String warningSign = "⚠️";
        return new EmbedBuilder()
        .setColor(Color.red)
        .setTitle(warningSign + " " + title + " " + warningSign)
        .setDescription(description)
        .setImage(url)
        .build();
    }

    public static MessageEmbed getDefaultSuccess(String title) {
        final String completeSign = "✅";
        return new EmbedBuilder()
        .setColor(Color.red)
        .setTitle(completeSign + " " +title)
        .build();
    }

    public static MessageEmbed getNotificationForJoin(String title){
        final String url = "https://media.giphy.com/media/Ae7SI3LoPYj8Q/giphy.gif";
        final String description = "Welcome to our server!";
        return new EmbedBuilder()
        .setColor(Color.green)
        .setTitle(title)
        .setDescription(description)
        .setImage(url)
        .build();
    }

    public static MessageEmbed getNotificationForLeave(String title){
        final String url = "https://media.giphy.com/media/TgsvriYRmA5Uq8xL08/giphy.gif";
        return new EmbedBuilder()
        .setColor(Color.ORANGE)
        .setTitle(title)
        .setImage(url)
        .build();
    }

}
