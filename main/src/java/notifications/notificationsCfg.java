package notifications;

import messengerChilds.childsCfg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class notificationsCfg {
    File configFile = new File("configs/notificationsCfg.properties");
    private static final Logger logger = LogManager.getLogger(notificationsCfg.class);
    public notificationsCfg() {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String numbs = props.getProperty("notifsNumber");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}
