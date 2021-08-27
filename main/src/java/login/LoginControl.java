package login;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import jsonContoller.jsonUsers;
import mainPages.Feed;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;

public class LoginControl {

    Scanner input = new Scanner(System.in);
    private final String[] log_info = new String[2];

    public LoginControl() {
    }

    private static final Logger logger = LogManager.getLogger(LoginControl.class);

    public static void loginAction(Label info, String[] log_info, TextField passWord, Stage stage, TextField userName) throws Exception {


        logger.info("System: user went to LogInfo");
        var Get=new jsonUsers();

        List<objUsers> users;
        users = Get.get();
        boolean nf = false;

        for(int i = 0; i < users.size(); i++) {
            if (log_info[0].equalsIgnoreCase(users.get(i).getUsername()) &&
                    log_info[1].equals(users.get(i).getPassword())) {

                logger.info("System: User logged in");
                JOptionPane.showMessageDialog(null, "You logged in Successfully!");
                stage.close();
                users.get(i).setLastseen("Online");
                new jsonUsers(users);
                new Feed(users.get(i).getUsername());
                nf = true;
                break;
            } else if (log_info[0].equalsIgnoreCase(users.get(i).getUsername())) {

                logger.info("System: User inserted wrong password");
                info.setText("Your Password was wrong, please try again!");
                info.setTextFill(Color.RED);
                passWord.getStyleClass().add("wrong");
                Timeline Normalizer = null;
                Normalizer = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
                    passWord.getStyleClass().remove("wrong");
                    info.setText("Login or creat an account");
                    info.setTextFill(Color.web("#2196f3"));
                }));
                Normalizer.setCycleCount(0);
                Normalizer.play();
                nf = true;
                break;

            }
        }
        if (!nf){
            logger.info("System: User inserted wrong username and password");
            info.setText("Your Username or Password was wrong, please try again!");
            info.setTextFill(Color.RED);
            passWord.getStyleClass().add("wrong");
            userName.getStyleClass().add("wrong");
            Timeline Normalizer = null;
            Normalizer = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
                passWord.getStyleClass().remove("wrong");
                userName.getStyleClass().remove("wrong");
                info.setText("Login or creat an account");
                info.setTextFill(Color.web("#2196f3"));
            }));
            Normalizer.setCycleCount(0);
            Normalizer.play();
        }
    }

    public void log_info(String log) throws IOException, URISyntaxException {

        return;
    }
}

