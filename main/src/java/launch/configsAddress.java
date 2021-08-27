package launch;

import category.catrgoryCfg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class configsAddress {

    public static String category;
    public static String chat;
    public static String comments;
    public static String edit;
    public static String graphics;
    public static String groups;
    public static String lists;
    public static String login;
    public static String mainPages;
    public static String child;
    public static String notifications;
    public static String objects;
    public static String register;
    public static String reports;
    public static String submit;
    public static String control;
    public static String userControl;
    public static String json;
    public static String launch;
    public static String imageControl;

    private static final Logger logger = LogManager.getLogger(configsAddress.class);
    public configsAddress() throws URISyntaxException {
        String configs_dir = System.getenv("configs_address_path");
        Path simple = Paths.get("simple.png");
        Path path = Paths.get(simple.toAbsolutePath().getParent() +configs_dir);
        File configFile = new File(String.valueOf(path));
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            category = props.getProperty("category");
            chat = props.getProperty("chat");
            child = props.getProperty("child");
            comments = props.getProperty("comments");
            control = props.getProperty("control");
            edit = props.getProperty("edit");
            graphics = props.getProperty("graphics");
            groups = props.getProperty("groups");
            json = props.getProperty("json");
            launch = props.getProperty("launch");
            lists = props.getProperty("list");
            login = props.getProperty("login");
            mainPages = props.getProperty("main");
            notifications = props.getProperty("notifications");
            objects = props.getProperty("objects");
            register = props.getProperty("register");
            reports = props.getProperty("reports");
            submit = props.getProperty("submit");
            userControl = props.getProperty("user");
            imageControl = props.getProperty("image");


            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}
