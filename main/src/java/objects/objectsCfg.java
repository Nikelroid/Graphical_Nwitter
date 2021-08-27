package objects;

import notifications.notificationsCfg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class objectsCfg {

    File configFile = new File("configs/objectsCfg.properties");
    private static final Logger logger = LogManager.getLogger(objectsCfg.class);
    public objectsCfg() {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String maps = props.getProperty("objectsMaps");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}
