package twitteControl;

import graphics.selectIcon;
import imageControl.getImageFile;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import mainPages.*;
import objects.objTwitte;
import objects.objUsers;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reports.Reports;
import submit.submitComment;
import submit.submitLike;
import submit.submitRetwitte;
import submit.submitShare;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    public Menu() {
    }

    jsonUsers Users_get = new jsonUsers();
    List<objUsers> users = Users_get.get();
    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    private static final Logger logger = LogManager.getLogger(Menu.class);
    public void Menu_command(String username) {
        Button Feed = (Button) launch.view.scene.lookup("#feed");
        Button Explorer = (Button) launch.view.scene.lookup("#expelorer");
        Button Home = (Button) launch.view.scene.lookup("#home");
        Button Messenger = (Button) launch.view.scene.lookup("#messenger");
        Button Setting = (Button) launch.view.scene.lookup("#setting");

        userFinder uf = new userFinder();
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

        Feed.setOnAction(event -> {
            try {
                new Feed(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Explorer.setOnAction(event -> {
            try {
                new Expelorer(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Home.setOnAction(event -> {
            try {
                new Info(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Messenger.setOnAction(event -> {
            if (users.get(uf.UserFinder(username)).getIsEnable()) {
                try {
                    new Messenger(username);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                JOptionPane.showMessageDialog(null,"You cant access messages" +
                        " when your account is disable");
            }
        });
        Setting.setOnAction(event -> {
            try {
                new Setting(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void twitteButtons(String username, int[] mapper, int counter) throws Exception {
        userFinder uf = new userFinder();
        if (users.get(uf.UserFinder(username)).getIsEnable()) {
            for (int i = 1; i < counter; i++) {
                final int finalI = i;
                TwitteController.likeList.get(i - 1).setOnAction(event -> {
                    submitLike Like = new submitLike();
                    try {
                        Like.list(mapper, finalI, counter, username);
                    } catch (IOException e) {
                        logger.error("error in main list of likes");
                    }
                });
                TwitteController.likeAction.get(i - 1).setOnAction(event -> {
                    new submitLike(username, mapper, finalI, counter, 1);
                });
                TwitteController.commentList.get(i - 1).setOnAction(event -> {
                    submitComment Com = new submitComment();
                    try {
                        Com.list(mapper, finalI, counter, username);
                    } catch (IOException e) {
                        logger.error("Error: in loading comment list");
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                TwitteController.commentAction.get(i - 1).setOnAction(event -> {
                    new submitComment(username, mapper, finalI, counter, 1);
                });
                TwitteController.retwitteList.get(i - 1).setOnAction(event -> {
                    submitRetwitte Ret = new submitRetwitte();
                    try {
                        Ret.list(mapper, finalI, counter, username);
                    } catch (IOException e) {
                        logger.error("error in main list of rets");
                    }
                });
                TwitteController.retwitteAction.get(i - 1).setOnAction(event -> {
                    new submitRetwitte(username, mapper, finalI, counter, 1);
                });
                TwitteController.Share.get(i - 1).setOnAction(event -> {
                    try {
                        new submitShare(username, finalI, mapper);
                    } catch (IOException e) {
                        logger.error("Error in sharing twitte");
                    }
                });
                TwitteController.Save.get(i - 1).setOnAction(event -> {
                    submitShare sS = new submitShare();
                    sS.submitSave(username, finalI, mapper, 1);
                });
                TwitteController.Report.get(i - 1).setOnAction(event -> {
                    String reptext = JOptionPane.showInputDialog("Insert your philosophy of report");
                    new Reports(username, reptext, mapper, finalI, counter);
                });
                TwitteController.twitteImages.get(i-1).setOnMouseClicked(mouseEvent -> {
                    try {
                        getImageFile getProfileFile = new getImageFile();
                        File file = getProfileFile.twitte(  twittes.get(mapper[finalI-1]).getSerial()+"");
                        Image image = new Image(file.toURI().toString());
                        FileUtils.readFileToByteArray(file);
                        new selectIcon(file);
                    } catch (NullPointerException| FileNotFoundException ignored) {
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        }

        public void buttonsStyleSetter ( int counter, int i){
            if (twittes.get(i).getLikes().contains(Feed.username)) {

                TwitteController.likeAction.get(counter - 1).getStyleClass().add("liked");
                TwitteController.likeAction.get(counter - 1).setText("Unlike");
            }
            if (twittes.get(i).getRetwittes().contains(Feed.username)) {
                TwitteController.retwitteAction.get(counter - 1).getStyleClass().add("retwitted");
                TwitteController.retwitteAction.get(counter - 1).setText("Retwitted");
            }
            userFinder us = new userFinder();
            int target = us.UserFinder(Feed.username);
            if (users.get(target).getTwittesaved().contains(twittes.get(i).getSerial())) {
                TwitteController.Save.get(counter - 1).getStyleClass().add("saved");
                TwitteController.Save.get(counter - 1).setText("Remove");
            }
        }

}
