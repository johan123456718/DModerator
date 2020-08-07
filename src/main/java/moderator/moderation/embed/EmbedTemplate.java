package moderator.moderation.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

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

}
