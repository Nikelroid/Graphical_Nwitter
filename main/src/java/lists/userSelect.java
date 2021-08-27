package lists;

import category.categoryMembers;
import imageControl.getImageFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jsonContoller.jsonUsers;
import objects.objUsers;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class userSelect {
    AnchorPane card;
    ImageView Icon;
    Label Username ,Name;
    jsonUsers get_j = new jsonUsers();
    List<objUsers> users = get_j.get();
    userFinder us = new userFinder();
    int target=0;
    Parent selectRoot;
    Scene selectScene;
    Stage selectStage;
    public static ArrayList<CheckBox> select ;

    private static final Logger logger = LogManager.getLogger(categoryMembers.class);
    private void definer() throws IOException {
        card = FXMLLoader.load(getClass().getResource("/layout/cards/user_select.fxml"));
        Username = (Label) card.lookup("#username");
        Name = (Label) card.lookup("#name");
        Icon = (ImageView) card.lookup("#icon");

    }

    private void setter (int userNum){
        try {
            try {
                getImageFile getProfileFile = new getImageFile();
                File file = getProfileFile.profile( users.get(userNum).getUsername()+"");
                Image image = new Image(file.toURI().toString());
                FileUtils.readFileToByteArray(file);
                Icon.setImage(image);
            } catch (NullPointerException| FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (NullPointerException ignored){
        }
        select.add((CheckBox) card.lookup("#select"));
        Username.setText(users.get(userNum).getUsername());
        Name.setText(users.get(userNum).getName());
    }
    private void adder () {
        ScrollPane scrollPane = (ScrollPane) selectScene.lookup("#scobar");
        VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
        twitteList.getChildren().add(card);
    }



    public userSelect(String username) throws IOException {
        target = us.UserFinder(username);
         selectRoot = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));
         selectScene = new Scene(selectRoot);
         selectStage = new Stage();
        selectStage.setScene(selectScene);
        selectStage.show();

        userFinder uf = new userFinder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ImageView Exit = (ImageView) selectScene.lookup("#exit") ;
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

        Button Back = (Button) selectScene.lookup("#back");
        Back.setText("Send");

        Label Header = (Label) selectScene.lookup("#header");
        Header.setText("Select followings");


            Back.setOnAction(event -> {
                try {
                    Back.getScene().getWindow().hide();
                } catch (Exception e) {
                    logger.error("Error in back to category page");
                }
            });


            for (int i = 1; i < users.get(target).getFollowings().size(); i++) {

                     definer();
                     setter(us.UserFinder(users.get(target).getFollowings().get(i)));
                     adder();

            }
        selectStage.close();
        selectStage.showAndWait();

    }
}
