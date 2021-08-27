package comments;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class commentsCfg {

    File configFile = new File("configs/commentsCfg.properties");
    private static final Logger logger = LogManager.getLogger(commentsCfg.class);
    public commentsCfg() {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String serial = props.getProperty("serial");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}

