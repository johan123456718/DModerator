package moderator.config;

import moderator.config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.NoSuchElementException;
import java.util.Optional;

public class Bot {
    private static JDA jda;
    private String token;

    public Bot(String fileName){
        Config.Load.instance(fileName);
        token = Config.Load.token();
        System.out.println("Finish loading bot configurations!");
    }

    public void run(){
        try {
            buildJDA();
            String botName =  getJDA().getSelfUser().getName();
            System.out.println(String.format("%s is now Running!",botName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void buildJDA() throws Exception{
        jda = JDABuilder.createDefault(token)
                .build();
        jda.awaitReady();

        System.out.println("Finish Building JDA!");
    }

    public static JDA getJDA(){
        Optional<JDA> jdaOrNull = Optional.ofNullable(jda);
        return jdaOrNull.orElseThrow(() -> new NoSuchElementException("JDA is null! Run `buildJDA()` first!"));
    }
}
