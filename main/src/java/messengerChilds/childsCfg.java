package messengerChilds;

import jsonContoller.jsonCfg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class childsCfg {

    File configFile = new File("configs/childsCfg.properties");
    private static final Logger logger = LogManager.getLogger(childsCfg.class);
    public childsCfg() {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String childsCode = props.getProperty("childs");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}
