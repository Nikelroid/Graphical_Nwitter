package twitteControl;

import notifications.notificationsCfg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class twitteCfg {

    File configFile = new File("configs/twitteCfg.properties");
    private static final Logger logger = LogManager.getLogger(twitteCfg.class);
    public twitteCfg() {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String tag = props.getProperty("twitteTag");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}
