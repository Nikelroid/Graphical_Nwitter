package imageControl;

import category.catrgoryCfg;
import launch.configsAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class imageControlCfg {


    public static String profilePath;
    public static String twittePath;
    public static String messagePath;

    private static final Logger logger = LogManager.getLogger(imageControlCfg.class);
    public imageControlCfg() {
        Path simple = Paths.get("simple.png");
        File configFile = new File(simple.toAbsolutePath().getParent()+configsAddress.imageControl);
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            profilePath = props.getProperty("icon_address");
            twittePath = props.getProperty("twitte_pic_address");
            messagePath = props.getProperty("message_pic_address");



            reader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            logger.error("file does not exist");
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("I/O error");
        }
    }
}
