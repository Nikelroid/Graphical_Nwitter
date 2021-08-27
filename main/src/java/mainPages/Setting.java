package mainPages;

import category.Category;
import edit.editProfile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import jsonContoller.jsonUsers;
import launch.view;
import login.loginView;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitDelet;
import submit.submitEdit;
import submit.submitPrivacy;
import twitteControl.Menu;
import userControl.userFinder;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Setting {
    submitEdit edt = new submitEdit();
    jsonUsers Users_get = new jsonUsers();
    List<objUsers> users = Users_get.get();
    String response;
    int target=0;
    private static final Logger logger = LogManager.getLogger(Setting.class);
    public static Button togglePrivate, toggleLastseen, toggleEmail, togglePhonenumber, toggleBirthday,
            Catrgories, Edit, Logout, toggleEnable,Delete;


    public Setting(String username) throws Exception {

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/setting_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.show();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ImageView Exit = (ImageView) launch.view.root.lookup("#exit") ;
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int response = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app and stay online?");
            if (response==0){
                System.exit(1);
            }else if (response==1) {
                users.get(target).setLastseen(dtf.format(now));
                new jsonUsers(users);
                System.exit(1);
            }
        });

        togglePrivate = (Button) launch.view.root.lookup("#toggle_private");
        toggleLastseen = (Button) launch.view.root.lookup("#toggle_lastseen");
        toggleBirthday = (Button) launch.view.root.lookup("#toggle_birthday");
        toggleEmail = (Button) launch.view.root.lookup("#toggle_email");
        togglePhonenumber = (Button) launch.view.root.lookup("#toggle_phonenumber");
        Catrgories = (Button) launch.view.root.lookup("#categories");
        Edit = (Button) launch.view.root.lookup("#edit");
        Logout = (Button) launch.view.root.lookup("#logout");
        toggleEnable = (Button) launch.view.root.lookup("#toggle_enable");
        Delete = (Button) launch.view.root.lookup("#delete");

        logger.info("System: user went to mainPages.Setting");

        var menu = new Menu();
        menu.Menu_command(username);
        var uf = new userFinder();
        target = uf.UserFinder(username);





        Delete.setOnAction(event -> {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure?");
            if (input==0) {
                new submitDelet(username);
                logger.info("System: " + username + "Account deleted");
                try {
                    new loginView(true);
                } catch (Exception e) {
                    logger.error("System: error in reload login page");
                }
            }
        });

        if(users.get(target).getAccount()){
            togglePrivate.getStyleClass().remove("toggle_wrong");
            togglePrivate.setText("Account: Public");
                }else{
            togglePrivate.getStyleClass().add("toggle_wrong");
            togglePrivate.setText("Account: Private");
                }

        togglePrivate.setOnAction(event -> {
            if(edt.editPrivacy(username)){
                togglePrivate.getStyleClass().remove("toggle_wrong");
                togglePrivate.setText("Account: Public");
            }else{
                togglePrivate.getStyleClass().add("toggle_wrong");
                togglePrivate.setText("Account: Private");
            }
        });


        if(users.get(target).getIsEnable()){
            toggleEnable.getStyleClass().remove("toggle_wrong");
            toggleEnable.setText("Account is Enable");
        }else{
            toggleEnable.getStyleClass().add("toggle_wrong");
            toggleEnable.setText("Account is Disable");
        }

        toggleEnable.setOnAction(event -> {
            if(edt.editStatus(username)){
                toggleEnable.getStyleClass().remove("toggle_wrong");
                toggleEnable.setText("Account is Enable");
            }else{
                toggleEnable.getStyleClass().add("toggle_wrong");
                toggleEnable.setText("Account is Disable");
            }
        });

        Edit.setOnAction(event -> {
            try {
                new editProfile(username);
            } catch (Exception e) {
                logger.error("error in loading edit info page");
            }
        });

        Catrgories.setOnAction(event -> {
            if (users.get(uf.UserFinder(username)).getIsEnable()) {
                try {
                    new Category(username);
                } catch (Exception e) {
                    logger.error("Error in open categories from mainPages.Info");
                }
            }else{
                JOptionPane.showMessageDialog(null,"You cant access categories" +
                        " when your account is disable");
            }
        });
        Logout.setOnAction(event -> {
            users.get(target).setLastseen(dtf.format(now));
                new jsonUsers(users);
            try {
                new loginView(true);
            } catch (Exception e) {
                logger.error("Error in loading login page");
            }
        });

                new submitPrivacy(username);

    }
}
