package submit;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitteControl.TwitteController;
import userControl.userFinder;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class submitShare {
    jsonTwittes Get = new jsonTwittes();
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    List<objTwitte> twittes = Get.get();
    int[] userMapper = new int[users.size()];
    int target = 0,userCounter = 1;
    submitMessage sb = new submitMessage();

    private static final Logger logger = LogManager.getLogger(submitShare.class);

    public submitShare() {}
        public submitShare(String username , int i,int[] mapper) throws IOException {

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
            Header.setText("Share with:");

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


            ArrayList<Button> share  = new ArrayList<>();


        for (int j = 0; j < users.size(); j++)
            if (users.get(target).getFollowings().contains(users.get(j).getUsername())
                    && users.get(j).getIsEnable()) {

                AnchorPane card = FXMLLoader.load(getClass().getResource("/layout/cards/category_members_card.fxml"));
                Label Username = (Label) card.lookup("#username");
                Label Name = (Label) card.lookup("#name");
                ImageView icon = (ImageView) card.lookup("#icon");
                Username.setText(users.get(j).getUsername());
                Name.setText(users.get(j).getUsername());

                ScrollPane scrollPane = (ScrollPane) ListScene.lookup("#scobar");
                VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
                share.add((Button) card.lookup("#delete"));
                twitteList.getChildren().add(card);
                userMapper[userCounter - 1] = j;
                userCounter++;
            }
            for (int j = 0; j < share.size(); j++) {
                share.get(j).setText("Share");
                share.get(j).getStyleClass().remove("toggle_wrong");
                share.get(j).getStyleClass().add("login");
            }
            Back.setOnAction(event -> {
                Back.getScene().getWindow().hide();
            });
            for (int j = 0; j < share.size(); j++) {
                final int finalJ = j;
                share.get(j).setOnAction(event -> {
                    sb.SubMess("^Forwarded Twitte from " + twittes.get(mapper[i - 1]).getSender()
                                    + " : " + twittes.get(mapper[i - 1]).getText(), username,
                            users.get(userMapper[finalJ]).getUsername());
                    new jsonUsers(users);
                    logger.info("System: Twitte Forwarded");
                    JOptionPane.showMessageDialog(null, "Twitte Forwarded");
                    Back.getScene().getWindow().hide();
                });
            }
    }
    public void submitSave(String username,int i,int[] mapper,int type){
        i = i-1;
        userFinder us = new userFinder();
        int target = us.UserFinder(username);
        if(users.get(target).getTwittesaved().contains(twittes.get(mapper[i]).getSerial())) {
            for (int k = 0; k < users.get(target).getTwittesaved().size(); k++)
                if (users.get(target).getTwittesaved().get(k).equals(twittes.get(mapper[i]).getSerial())) {
                    users.get(target).getTwittesaved().remove(k);
                    break;
                }
            if (type==1) {
                TwitteController.Save.get(i).getStyleClass().remove("saved");
                TwitteController.Save.get(i).setText("Save");
            }else if (type==2){
                TwitteController.cSave.get(i).getStyleClass().remove("saved");
                TwitteController.cSave.get(i).setText("Save");
            }
            logger.info("System: Twitte Unsaved");
        }else{
            if (type==1) {
            TwitteController.Save.get(i).getStyleClass().add("saved");
            TwitteController.Save.get(i).setText("Unsave");
            }else if (type==2){
                TwitteController.cSave.get(i).getStyleClass().add("saved");
                TwitteController.cSave.get(i).setText("Unsave");
            }
            users.get(target).getTwittesaved().add(twittes.get(mapper[i]).getSerial());
            logger.info("System: Twitte saved");
        }
        new jsonUsers(users);
    }
}
