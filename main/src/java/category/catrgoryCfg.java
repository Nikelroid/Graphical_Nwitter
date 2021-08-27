package category;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class catrgoryCfg {

    File configFile = new File("configs/categoryCfg.properties");
    private static final Logger logger = LogManager.getLogger(catrgoryCfg.class);
    public catrgoryCfg() {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String title = props.getProperty("header");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}

