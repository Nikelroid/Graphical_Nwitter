package register;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import jsonContoller.jsonUsers;
import login.loginView;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class registerView {


    String inputtext;
    public static Label info;
    public static TextField[] fields=new TextField[11];
    private static final Logger logger = LogManager.getLogger(registerView.class);
    public registerView() throws IOException {

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/register_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.setTitle("Register");

        userFinder uf = new userFinder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ImageView Exit = (ImageView) launch.view.root.lookup("#exit") ;
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int response = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app?");
            if (response==0){
                System.exit(1);
            }
        });

        Button Login = (Button) launch.view.scene.lookup("#login");
        Button register = (Button) launch.view.scene.lookup("#register");
        fields[0] = (TextField) launch.view.scene.lookup("#short_name");
        fields[1] = (TextField) launch.view.scene.lookup("#last_name");
        fields[2] = (TextField) launch.view.scene.lookup("#user_name");
        fields[3] = (TextField) launch.view.scene.lookup("#pass_word");
        fields[4] = (TextField) launch.view.scene.lookup("#c_pass");
        fields[5] = (TextField) launch.view.scene.lookup("#year");
        fields[6] = (TextField) launch.view.scene.lookup("#mounth");
        fields[7] = (TextField) launch.view.scene.lookup("#day");
        fields[8] = (TextField) launch.view.scene.lookup("#email");
        fields[9] = (TextField) launch.view.scene.lookup("#phone_number");
        fields[10] = (TextField) launch.view.scene.lookup("#bio");
        info = (Label) launch.view.scene.lookup("#info") ;

        register.setOnAction(event -> {
            try {
                new regChecker();
            } catch (Exception e) {
                logger.error("Error in getting register information");
            }
        });

        Login.setOnAction(event -> {
            try {
                new loginView(true);
            } catch (Exception e) {
                logger.error("Error in open login page (back to login)");
            }
        });

    }


    public static boolean intchecker(String checkcase){
        try{
            Long.parseLong(checkcase);
            return false;
        }catch (NumberFormatException exception){
            return true;
        }
    }

    public static boolean isstrong(String password){
        return password.toUpperCase().equals(password)
                || password.toLowerCase().equals(password);
    }

    public static void Normalizer(TextField[] fields, Label info){
        Timeline Normalizer = new Timeline();
        Normalizer = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
            for (int i = 0; i < 11; i++) {
                fields[i].getStyleClass().remove("wrong");
            }
            info.setText("REGISTER");
            info.setTextFill(Color.web("#2196f3"));
        }));
        Normalizer.setCycleCount(0);
        Normalizer.play();
    }

    public static boolean Checker(String input , int type){
        File f = new File("Users.json");
        if (f.exists()) {
            var get_j =new jsonUsers();
            List<objUsers> users = get_j.get();
            switch (type) {
                case 1:
                    for (objUsers user : users)
                        if (input.equalsIgnoreCase(user.getUsername()))
                            return true;
                    break;
                case 2:
                    for (objUsers user : users)
                        if (input.equals(user.getEmail()))
                            return true;
                    break;
                case 3:
                    for (objUsers user : users)
                        if (input.equals(user.getPhonenumber()))
                            return true;
                    break;
                }

            }

        return false;
        }
}
