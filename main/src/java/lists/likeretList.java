package lists;

import imageControl.getImageFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import objects.objTwitte;
import objects.objUsers;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitMessage;
import submit.submitShare;
import userControl.userFinder;
import userControl.userProfile;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class likeretList {
    jsonTwittes Get = new jsonTwittes();
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    List<objTwitte> twittes = Get.get();
    int[] userMapper = new int[users.size()];
    int target = 0,userCounter = 1;
    submitMessage sb = new submitMessage();

    private static final Logger logger = LogManager.getLogger(submitShare.class);

    public likeretList() {}
    public likeretList(String username , int i,int type) throws IOException {

        for (int j = 0; j < users.size(); j++) {
            if (users.get(j).getUsername().equals(username)) {
                target = j;
                break;
            }
        }

        Parent ListRoot = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));
        Scene ListScene = new Scene(ListRoot);
        Stage ListStage = new Stage();
        ListStage.setScene(ListScene);
        ListStage.show();

        userFinder uf = new userFinder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ImageView Exit = (ImageView) ListScene.lookup("#exit") ;
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

        Button Back = (Button) ListScene.lookup("#back");
        Back.setText("Back");
        Label Header = (Label) ListScene.lookup("#header");
        if (type==1)
        Header.setText("Likes");
        else
            Header.setText("Retwittes");


        ArrayList<Button> share = new ArrayList<>();


        for (int j = 0; j < users.size(); j++)
            if (type==1){
            if (twittes.get(i).getLikes().contains(users.get(j).getUsername())
                    && users.get(j).getIsEnable()
                    && !users.get(j).getBlocks().contains(username)) {

                AnchorPane card = FXMLLoader.load(getClass().getResource("/layout/cards/category_members_card.fxml"));
                Label Username = (Label) card.lookup("#username");
                Label Name = (Label) card.lookup("#name");
                ImageView Icon = (ImageView) card.lookup("#icon");

                    try {
                        getImageFile getProfileFile = new getImageFile();
                        File file = getProfileFile.profile( users.get(j).getUsername()+"");
                        Image image = new Image(file.toURI().toString());
                        FileUtils.readFileToByteArray(file);
                        Icon.setImage(image);
                    } catch (NullPointerException| FileNotFoundException ignored) {
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                Username.setText(users.get(j).getUsername());
                Name.setText(users.get(j).getUsername());

                ScrollPane scrollPane = (ScrollPane) ListScene.lookup("#scobar");
                VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
                share.add((Button) card.lookup("#delete"));
                twitteList.getChildren().add(card);
                userMapper[userCounter - 1] = j;
                userCounter++;
            }
            }else
            if (twittes.get(i).getRetwittes().contains(users.get(j).getUsername())
                    && users.get(j).getIsEnable()
                    && !users.get(j).getBlocks().contains(username)) {

                AnchorPane card = FXMLLoader.load(getClass().getResource("/layout/cards/category_members_card.fxml"));
                Label Username = (Label) card.lookup("#username");
                Label Name = (Label) card.lookup("#name");
                ImageView Icon = (ImageView) card.lookup("#icon");
                Username.setText(users.get(j).getUsername());
                Name.setText(users.get(j).getUsername());
                try {

                    try {
                        getImageFile getProfileFile = new getImageFile();
                        File file = getProfileFile.profile(users.get(j).getUsername()+"");
                        Image image = new Image(file.toURI().toString());
                        FileUtils.readFileToByteArray(file);
                        Icon.setImage(image);
                    } catch (NullPointerException| FileNotFoundException ignored) {
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }catch (NullPointerException ignored){
                }
                ScrollPane scrollPane = (ScrollPane) ListScene.lookup("#scobar");

                VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
                share.add((Button) card.lookup("#delete"));
                twitteList.getChildren().add(card);
                userMapper[userCounter - 1] = j;
                userCounter++;
            }

        for (int j = 0; j < share.size(); j++) {
            share.get(j).setText("Profile");
            share.get(j).getStyleClass().remove("toggle_wrong");
            share.get(j).getStyleClass().add("login");
        }
        Back.setOnAction(event -> {
            Back.getScene().getWindow().hide();
        });
        for (int j = 0; j < share.size(); j++) {
            final int finalJ = j;
            share.get(j).setOnAction(event -> {
                try {
                    new userProfile(username,users.get( userMapper[finalJ]).getUsername());
                } catch (Exception e) {
                    logger.error("Error in loading user profile");
                }
                Back.getScene().getWindow().hide();
            });
        }
    }
}
