package groups;

import comments.commentsCfg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class gpCfg {

    File configFile = new File("configs/gpCfg.properties");
    private static final Logger logger = LogManager.getLogger(gpCfg.class);
    public gpCfg() {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String serial = props.getProperty("gpSerials");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}
