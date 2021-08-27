package mainPages;

import comments.commentsCfg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class mainCfg {

    File configFile = new File("configs/mainCfg.properties");
    private static final Logger logger = LogManager.getLogger(mainCfg.class);
    public mainCfg() {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String mainParametrs = props.getProperty("main");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}
