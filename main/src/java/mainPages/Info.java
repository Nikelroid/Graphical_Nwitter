package mainPages;

import category.Category;
import edit.editProfile;
import graphics.selectIcon;
import imageControl.getImageFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonNotifs;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import lists.Lists;
import notifications.Notifications;
import objects.objNotifs;
import objects.objTwitte;
import objects.objUsers;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitRequest;
import twitteControl.Menu;
import twitteControl.Newtwitte;
import twitteControl.TwitteController;
import twitteControl.userTwittes;
import userControl.userController;
import userControl.userFinder;


import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Info {

    int target = 0;
    submitRequest subreq = new submitRequest();
    jsonNotifs Not = new jsonNotifs();
    List<objNotifs> notifs;
    boolean req = false;
    File n = new File("Notifs.json");
    Label followsYou, blocksYou, mutesYou;
    AnchorPane newTwitte;
    public Button followerCount, followingCount, blockCount, muteCount, categories, savedTwittes, notifications, edit, savedMessages;
    userControl.userFinder userFinder = new userFinder();
    jsonUsers get_j = new jsonUsers();
    List<objUsers> users;

    Menu menu = new Menu();

    jsonTwittes getTwittes = new jsonTwittes();

    public static ImageView Icon;
    public void refreshList(String username, String anouther_username) {
        users = get_j.get();
        notifs = Not.get();
        if (n.exists())
            for (int i = 0; i < notifs.size(); i++) {
                if (notifs.get(i).getUser1().equals(username) &&
                        notifs.get(i).getUser2().equals(anouther_username) &&
                        notifs.get(i).getType() == 8) {
                    req = true;
                    break;
                }
                if (i == notifs.size() - 1) req = false;
            }
        else notifs = new ArrayList<>();
    }

    public void resetCounts(String anouther_username) {
        target = userFinder.UserFinder(anouther_username);
        followerCount.setText(users.get(target).getFollowers().size() - 1 + "");
        followingCount.setText(users.get(target).getFollowings().size() - 1 + "");
        blockCount.setText(users.get(target).getBlocks().size() - 1 + "");
        muteCount.setText(users.get(target).getMutes().size() - 1 + "");
    }

    private static final Logger logger = LogManager.getLogger(Info.class);
    public Info(String username) throws Exception {

        logger.info("System: user went to userControl.userProfile");

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/user_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.show();
        Label Header = (Label) launch.view.scene.lookup("#header");

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

        TwitteController.likeList = new ArrayList<>();
        TwitteController.likeAction = new ArrayList<>();
        TwitteController.commentList = new ArrayList<>();
        TwitteController.commentAction = new ArrayList<>();
        TwitteController.retwitteList = new ArrayList<>();
        TwitteController.retwitteAction = new ArrayList<>();
        TwitteController.Share = new ArrayList<>();
        TwitteController.Save = new ArrayList<>();


        followerCount = (Button) launch.view.root.lookup("#follower_count");
        followingCount = (Button) launch.view.root.lookup("#following_count");
        blockCount = (Button) launch.view.root.lookup("#block_count");
        muteCount = (Button) launch.view.root.lookup("#mute_count");

        Icon = (ImageView) launch.view.root.lookup("#picture");

        categories = (Button) launch.view.root.lookup("#follow_action");
        savedTwittes = (Button) launch.view.root.lookup("#block_action");
        notifications = (Button) launch.view.root.lookup("#mute_action");
        edit = (Button) launch.view.root.lookup("#remove_action");
        savedMessages = (Button) launch.view.root.lookup("#send_message");

        followsYou = (Label) launch.view.root.lookup("#follows_you");
        mutesYou = (Label) launch.view.root.lookup("#mutes_you");
        blocksYou = (Label) launch.view.root.lookup("#blocks_you");

        new userController(username,username);
        refreshList(username, username);
        resetCounts(username);
        Header.setText("@"+users.get(target).getUsername());
        categories.setText("Categories");
        savedTwittes.setText("Saved twittes");
        edit.setText("Edit profile");
        notifications.setText("Notifications");
        savedMessages.setText("Saved message");

        try {
            getImageFile getProfileFile = new getImageFile();
            File file = getProfileFile.profile( username+"");
            Image image = new Image(file.toURI().toString());
            FileUtils.readFileToByteArray(file);
            Icon.setImage(image);
        } catch (NullPointerException| FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }


        menu.Menu_command(username);

        Icon.setOnMouseEntered(mouseEvent -> {

            Icon.getStyleClass().add("focused");
        });
        Icon.setOnMouseExited(mouseEvent -> {
            Icon.getStyleClass().remove("username");
        });

        Icon.setOnMouseClicked(mouseEvent -> {
            try {
                new selectIcon(username,1);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Error in open select image");
            }
        });

        if (users.get(target).getIsEnable()) {
            newTwitte = FXMLLoader.load(getClass().getResource("/layout/header/new_twitte.fxml"));
            TextArea twitteText = (TextArea) newTwitte.lookup("#twitte_text");

            Button send = (Button) newTwitte.lookup("#send");
            Button picture = (Button) newTwitte.lookup("#pic");

            ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
            VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
            twitteList.getChildren().add(newTwitte);

            picture.setOnAction(event -> {
                try {
                    selectIcon si = new selectIcon();
                    si.SelectImage();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("Error in open select image");
                }
                        if (selectIcon.ImageIm != null) {
                            ImageView iw = new ImageView(selectIcon.ImageIm);
                            launch.view.root.getChildrenUnmodifiable().add(iw);
                        }
            });



            send.setOnAction(event -> {
                if(!twitteText.getText().isEmpty()) {
                    try {
                        new Newtwitte(twitteText.getText(), username);
                    } catch (Exception e) {
                        logger.error("Error in creat a new nwitte");
                    }
                }
                    if (selectIcon.ImageIm != null) {
                        if(twitteText.getText().isEmpty()) {
                            try {
                                new Newtwitte(" ", username);
                            } catch (Exception e) {
                                logger.error("Error in creat a new nwitte");
                            }
                        }
                        List<objTwitte> twittes = getTwittes.get();
                        try {
                            Path simple = Paths.get("simple.png");
                            Path path = Paths.get(simple.toAbsolutePath().getParent() +
                                    "\\main\\src\\resources\\twittes\\" +
                                    (twittes.get(twittes.size() - 1).getSerial()) + ".png");
                            Files.copy(selectIcon.fileIm.toPath(), path);

                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }


                        selectIcon.fileIm = null;
                        selectIcon.ImageIm = null;
                    }
                    try {
                        new Info(username);
                    } catch (Exception e) {
                        logger.error("Error in refresh info");
                    }
            });
            launch.view.scene.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    if(!twitteText.getText().isEmpty()) {
                        try {
                            new Newtwitte(twitteText.getText(), username);

                        } catch (Exception e) {
                            logger.error("Error in creat a new nwitte");
                        }
                        try {
                            new Info(username);
                        } catch (Exception e) {
                            logger.error("Error in refresh info");
                        }
                    }
                }
            });
        }

        categories.setOnAction(event -> {
            if (users.get(uf.UserFinder(username)).getIsEnable()) {
                try {
                    new Category(username);
                } catch (Exception e) {
                    logger.error("Error in open categories from mainPages.Info");
                }
            }else{
                JOptionPane.showMessageDialog(null,
                        "You cant access categories" +
                        " when your account is disable");
            }

        });


        savedTwittes.setOnAction(event -> {
            try {
                new messengerChilds.savedTwittes(username);
            } catch (Exception e) {
                logger.error("Error in open saved Twittes from mainPages.Info");
            }
        });


        notifications.setOnAction(event -> {
            if (users.get(uf.UserFinder(username)).getIsEnable()) {
                try {
                    new Notifications(username,1);
                } catch (Exception e) {
                    logger.error("Error in open notification from mainPages.Info");
                    e.printStackTrace();
                }
            }else{
                JOptionPane.showMessageDialog(null,"You cant access notifications" +
                        " when your account is disable");
            }

        });
        edit.setOnAction(event -> {
            try {
                new editProfile(username);
            } catch (Exception e) {
                logger.error("Error in open edit page from mainPages.Info");
            }
        });

        savedMessages.setOnAction(event -> {
            try {
                new messengerChilds.savedMessages(username);
            } catch (Exception e) {
                logger.error("Error in open saved message from mainPages.Info");

            }
        });

        followingCount.setOnAction(event -> {

                if (users.get(target).getFollowings().size() == 1)
                    JOptionPane.showMessageDialog(null, (username + " dont follow anybody!"));
                else
                    try {
                        new Lists(username, username, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

        });
        followerCount.setOnAction(event -> {
            if (users.get(target).getFollowers().size() == 1)
                JOptionPane.showMessageDialog(null, "Nobody follows " + username);
            else
                try {
                    new Lists(username, username, 2);
                } catch (Exception e) {
                    e.printStackTrace();
            }
        });
        blockCount.setOnAction(event -> {
            if (users.get(target).getBlocks().size() == 1)
                JOptionPane.showMessageDialog(null, (username + " dont block anybody"));
            else
                try {
                    new Lists(username, username, 3);
                } catch (Exception e) {
                    e.printStackTrace();
            }
        });
        muteCount.setOnAction(event -> {
                if (users.get(target).getMutes().size() == 1)
                    JOptionPane.showMessageDialog(null, (username + " dont mute anybody"));
                else
                    try {
                        new Lists(username, username, 4);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        });

            new userTwittes(username,username);

    }
}
