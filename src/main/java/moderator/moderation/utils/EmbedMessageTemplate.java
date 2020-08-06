package moderator.moderation.utils;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EmbedMessageTemplate implements Cloneable{
    private Map<String, EmbedBuilder> embeds;

    public EmbedMessageTemplate(){
        embeds = new HashMap<>();
        createEmbeds();
    }

    private void createEmbeds(){
        EmbedBuilder errorEmbed = new EmbedBuilder().setColor(Color.RED).setTitle("⚠️ ERROR");
        EmbedBuilder successEmbed = new EmbedBuilder().setColor(Color.green).setTitle("\uD83C\uDF89️ SUCCESS️");

        embeds.put("ERROR", errorEmbed);
        embeds.put("SUCCESS", successEmbed);
    }

    public EmbedBuilder getEmbedBuilder(String type){
        return embeds.getOrDefault(type, new EmbedBuilder());
    }

}
