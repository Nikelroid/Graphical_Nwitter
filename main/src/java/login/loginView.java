package login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import jsonContoller.jsonUsers;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import register.registerController;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class loginView {



    List<objUsers> users;
    private boolean logged = true;
    private static final Logger logger = LogManager.getLogger(loginView.class);

    String[] log_info = new String[2];
    Button login ;
    Button register ,Hack ;
    TextField userName ;
    TextField passWord ;
    Label Info ;

    public loginView() {
    }

    public loginView(boolean account) throws Exception {

        logger.info("System: user went to Login");


        Parent root = FXMLLoader.load(getClass().getResource("/layout/page/login_page.fxml"));
        launch.view.scene = new Scene(root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.setTitle("Login");

        userFinder uf = new userFinder();

        ImageView Exit = (ImageView) launch.view.root.lookup("#exit") ;
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int response = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app ?");
            if (response==0){
                System.exit(1);
            }
        });

        File f = new File("Users.json");
        var Register = new registerController();

        login = (Button) launch.view.scene.lookup("#login");
        register = (Button) launch.view.scene.lookup("#register");
        Hack = (Button) launch.view.scene.lookup("#hack");
        userName = (TextField ) launch.view.scene.lookup("#username");
        passWord = (TextField ) launch.view.scene.lookup("#password");
        Info = (Label ) launch.view.scene.lookup("#info");
        var Get=new jsonUsers();
        List<objUsers> users = Get.get();

        if (f.exists() || users!=null) {
            register.setOnAction(event -> {
                        try {
                            Register.Registeruser();
                        } catch (IOException | URISyntaxException e) {
                            logger.error("System: problem in opening register");
                        } catch (Exception e) {
                            logger.error("Error in opening message");
                        }
            });
            login.setOnAction(event -> {

                log_info[0]=userName.getText();
                log_info[1]=passWord.getText();
                try {
                    LoginControl.loginAction(Info, log_info, passWord, launch.view.stage, userName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            launch.view.scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    log_info[0] = userName.getText();
                    log_info[1] = passWord.getText();
                    try {
                        LoginControl.loginAction(Info, log_info, passWord, launch.view.stage, userName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (event.getCode() == KeyCode.H) {
                    Hack.setVisible(true);
                }
            });
            Hack.setOnAction(event -> {
                if (!users.get(uf.UserFinder(userName.getText())).getPassword().isEmpty())
                    JOptionPane.showMessageDialog(null,"Password :"+
                            users.get(uf.UserFinder(userName.getText())).getPassword());

            });
        }else {
            JOptionPane.showMessageDialog(
                    null, "You don't have an account, please create one!");
            Register.Registeruser();
        }
    }



}
