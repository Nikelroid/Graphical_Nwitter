package lists;

import comments.commentsCfg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class listCfg {

    File configFile = new File("configs/listCfg.properties");
    private static final Logger logger = LogManager.getLogger(listCfg.class);
    public listCfg() {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String listCode = props.getProperty("listMembers");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}
