package chat;

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
import jsonContoller.jsonUsers;
import objects.objUsers;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitMessage;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class newChat {
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    submitMessage sb = new submitMessage();

    int[] userMapper = new int[users.size()];
    int target=0,counter = 0;
    int[] mapper = new int[10000];
    private static final Logger logger = LogManager.getLogger(newChat.class);
    public newChat(String username) throws Exception {
        logger.info("System: user went to chat.newChat");

        var uf = new userFinder();
        target= uf.UserFinder(username);

        Parent ListRoot = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));
        Scene ListScene = new Scene(ListRoot);
        Stage ListStage = new Stage();
        ListStage.setScene(ListScene);
        ListStage.show();
        Button Back = (Button) ListScene.lookup("#back");
        Back.setText("Cancel");
        Label Header = (Label) ListScene.lookup("#header");
        Header.setText("New chat with:");

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

        ArrayList<Button> share  = new ArrayList<>();

        for (int j = 0; j < users.size(); j++)
            if (users.get(target).getFollowings().contains(users.get(j).getUsername())
                    && users.get(j).getIsEnable()) {

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

                userMapper[counter] = j;
                counter++;

            }

        Back.setOnAction(event -> {
            Back.getScene().getWindow().hide();
        });

        for (int j = 0; j < share.size(); j++) {
            share.get(j).setText("Start");
            share.get(j).getStyleClass().remove("toggle_wrong");
            share.get(j).getStyleClass().add("toggle_accept");
            final int finalJ = j;
            share.get(j).setOnAction(event -> {
                try {
                    new chatPage(username,users.get(userMapper[finalJ]).getUsername());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("System: New chat created");
                Back.getScene().getWindow().hide();
            });
        }


    }
}
