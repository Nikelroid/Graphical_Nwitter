package edit;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import jsonContoller.jsonUsers;
import mainPages.Info;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import register.registerView;
import submit.submitEdit;
import userControl.userFinder;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class editProfile {

    int target=0;
    private static final Logger logger = LogManager.getLogger(editProfile.class);
    public static TextField[] fields=new TextField[11];
    public static Label info;
    public static String username = null;
    public editProfile(String username) throws Exception {
        
        logger.info("System: user went to edit.editProfile");
        editProfile.username = username;
        userFinder uf = new userFinder();
        target = uf.UserFinder(editProfile.username);
        var get_j = new jsonUsers();
        List<objUsers> users = get_j.get();
        
        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/register_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.setTitle("Register");

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
                users.get(uf.UserFinder(username)).setLastseen(dtf.format(now));
                new jsonUsers(users);
                System.exit(1);
            }
        });

        Button Back = (Button) launch.view.scene.lookup("#login");
        Button editAction = (Button) launch.view.scene.lookup("#register");
        editAction.setText("Edit");
        Back.setText("Back");
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

        Label Header = (Label) launch.view.scene.lookup("#header") ;
        Header.setText("Edit Profile");
        info.setText("Fill fields which you want to edit");
        int regchecked = 0;
        submitEdit edit = new submitEdit();
        Back.setOnAction(event -> {
            try {
                new Info(editProfile.username);
            } catch (Exception e) {
                logger.info("Error in back to home");
            }
        });
        editAction.setOnAction(event -> {
            boolean edited = false;
            if (!fields[0].getText().isEmpty() || !fields[1].getText().isEmpty() ) {
                if ((fields[0].getText() + " " + fields[1].getText()).equals(users.get(target).getName())) {
                    errorSetter(0, "New name can't be old one");
                    return;
                } else {
                    edited=true;
                    edit.editName(editProfile.username, fields[0].getText() + " " + fields[1].getText());
                }
            }
            if (!fields[2].getText().isEmpty()) {
                if ((fields[2].getText()).equals(users.get(target).getUsername())) {
                    errorSetter(2, "New username can't be old one");
                    return;
                } else if (fields[2].getText().length() < 3) {
                    errorSetter(2, "Username is too short");
                    return;
                } else if (registerView.Checker(fields[2].getText(), 1)) {
                    errorSetter(2, "Username already chosen");
                    return;
                } else {
                    edited=true;
                    try {
                        edit.editUsername(editProfile.username, fields[2].getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editProfile.username=fields[2].getText();
                }
            }
            if (!fields[3].getText().isEmpty()||!fields[4].getText().isEmpty()) {
                if (fields[3].getText().equals(users.get(target).getPassword())) {
                    errorSetter(3, "New password can't be old one");
                    return;

                } else if (!fields[3].getText().equals(fields[4].getText())) {
                    errorSetter(3, "Password and Confirm password don't match");
                    fields[4].getStyleClass().add("wrong");
                    return;
                } else if (fields[3].getText().length() < 7) {
                    errorSetter(3, "Password is too short");
                    return;
                } else if (!registerView.intchecker(fields[3].getText())) {
                    errorSetter(3, "Password must contains letter or symbol");
                    return;
                } else if (registerView.isstrong(fields[3].getText())) {
                    errorSetter(3, "Password must contains a lowercase and uppercase letter an least");
                    return;
                } else {

                    String oldpass = JOptionPane.showInputDialog("Input old password for confirm");
                    if (oldpass.equals(users.get(target).getPassword())) {
                        edited = true;
                        edit.editPassword(editProfile.username, fields[3].getText());
                    }
                }
            }
            if (!fields[5].getText().isEmpty()) {
                if (registerView.intchecker(fields[5].getText())) {
                    errorSetter(5, "Year must be a number");
                    return;
                } else if (Integer.parseInt(fields[5].getText()) > 2003 ||
                        Integer.parseInt(fields[5].getText()) < 1921) {
                    errorSetter(5, "Year must be a number between 1921 to 2003");
                    return;
                } else if (registerView.intchecker(fields[6].getText())) {
                    errorSetter(6, "mounth must be a number");
                    return;
                } else if (Integer.parseInt(fields[6].getText()) > 12 ||
                        Integer.parseInt(fields[6].getText()) < 1) {
                    errorSetter(6, "Mounth must be a number between 1 to 12");
                    return;
                } else if (registerView.intchecker(fields[7].getText())) {
                    errorSetter(7, "Day must be a number");
                    return;
                } else if (Integer.parseInt(fields[7].getText()) > 31 ||
                        Integer.parseInt(fields[7].getText()) < 1) {
                    errorSetter(7, "Day must be a number between 1 and 31");
                    return;
                } else {
                    if ((fields[5].getText() + "/" + fields[6].getText() + "/" + fields[7].getText()).equals(
                            users.get(target).getBirthday())) {
                        errorSetter(5, "New birthday date can't be old one");
                        fields[5].getStyleClass().add("wrong");
                        fields[7].getStyleClass().add("wrong");
                    }
                    edit.editBithdaydate(editProfile.username,
                            fields[5].getText() + "/" + fields[6].getText() + "/" + fields[7].getText());
                    edited=true;
                }
            }
            if (!fields[8].getText().isEmpty()) {
                if (fields[8].getText().equals(users.get(target).getEmail())) {
                    errorSetter(8, "New email can't be old one");
                    return;
                } else if (registerView.Checker(fields[8].getText(), 2)) {
                    errorSetter(8, "This Email registered before");
                    return;
                } else {
                    edit.editEmail(editProfile.username, fields[8].getText());
                    edited=true;
                }
            }
            if (!fields[9].getText().isEmpty()) {
                if (fields[9].getText().equals(users.get(target).getPhonenumber())) {
                    errorSetter(9, "New phonenumber must be old one");
                } else if (registerView.intchecker(fields[9].getText())) {
                    errorSetter(9, "Phonenumber must be a number (or empty)");
                    return;
                } else if (registerView.Checker(fields[9].getText(), 3)) {
                    errorSetter(9, "This Phonenumber registered before");
                    return;
                } else {
                    edit.editPhonenumber(editProfile.username, fields[9].getText());
                    edited=true;
                }
            }
            if (!fields[10].getText().isEmpty()) {
                if (fields[10].getText().equals(users.get(target).getBio()))
                    errorSetter(10, "New Bio must be old one");
                else {
                    edit.editBio(editProfile.username, fields[10].getText());
                    edited=true;
                }
            }
            if (edited)
            JOptionPane.showMessageDialog(null,"Filled fields edited");
        });

    }
    public void errorSetter(int i , String errTxt){
        info.setText(errTxt);
        info.setTextFill(Color.RED);
        fields[i].getStyleClass().add("wrong");
        registerView.Normalizer(fields, info);
    }
    }

