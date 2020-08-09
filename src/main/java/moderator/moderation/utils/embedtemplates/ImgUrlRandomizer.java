package moderator.moderation.utils.embedtemplates;

import moderator.config.BotConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ImgUrlRandomizer {

    public static class ImageFile{
        public static final String ERROR_URL = "/img/error_imgs.txt";
        public static final String SUCCESS_URL = "/img/success_imgs.txt";
    }
    private final static Logger LOGGER = LogManager.getLogger(ImgUrlRandomizer.class);

    private final long LAST_LINE_IN_FILE = 99;

    public String getRandomImgURL(String file){
        try(RandomAccessFile fileWithURLS = new RandomAccessFile(getImgURLFile(file), "r")){
            return randomUrl(fileWithURLS);
        } catch (Exception e){
            LOGGER.error("File not found! Returning null image url...\n" + e.getMessage());
            return null;
        }
    }

    private File getImgURLFile(String file){
        try{
            return new File(getClass().getResource(file).toURI());
        } catch (URISyntaxException e){
            LOGGER.error("Bad URL!\n" + e.getMessage());
            throw new NullPointerException("No file retrieved!");
        }
    }

    //  From https://stackoverflow.com/a/53673751
    private String randomUrl(RandomAccessFile file) throws IOException {
        long fileLength = file.length() - LAST_LINE_IN_FILE;
        long randomPosition = ThreadLocalRandom.current().nextLong(fileLength); // generates a position from 0 to fileLength
        file.seek(randomPosition); //move file pointer to random position
        file.readLine(); //move file pointer to the start of next line
        String randomFullLine = file.readLine();

        if(randomFullLine == null) throw new NullPointerException("Error in randomUrl method");
        return getURLFromLine(randomFullLine);
    }

    private String getURLFromLine(String randomLine){
        return randomLine.split(" ")[0];
    }

}
