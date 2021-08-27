package userControl;

import chat.chatPage;
import graphics.selectIcon;
import imageControl.getImageFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jsonContoller.jsonNotifs;
import jsonContoller.jsonUsers;
import lists.Lists;
import mainPages.Info;
import objects.objNotifs;
import objects.objUsers;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitAction;
import submit.submitRequest;
import twitteControl.Menu;
import twitteControl.TwitteController;
import twitteControl.userTwittes;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class userProfile {
    int target = 0;
    submitRequest subreq = new submitRequest();
    jsonNotifs Not = new jsonNotifs();
    List<objNotifs> notifs;
    boolean req = false;
    boolean self = false;
    submitAction submit = new submitAction();
    File n = new File("Notifs.json");
    private static final Logger logger = LogManager.getLogger(userProfile.class);
    Label followsYou, blocksYou, mutesYou;
    public Button followerCount, followingCount, blockCount, muteCount, followAction, blockAction, muteAction, removeAction, sendMessage;
    userFinder userFinder = new userFinder();
    jsonUsers get_j = new jsonUsers();
    List<objUsers> users;
    Menu menu = new Menu();

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

    public userProfile(String username, String anouther_username) throws Exception {
        if (username.equals(anouther_username))
            new Info(username);
        else {

            logger.info("System: user went to userControl.userProfile");

            launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/user_page.fxml"));
            launch.view.scene = new Scene(launch.view.root);
            launch.view.stage.setScene(launch.view.scene);
            launch.view.stage.show();

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

            Label Header = (Label) launch.view.scene.lookup("#header");
            Header.setText("@" + anouther_username);
            TwitteController.likeList = new ArrayList<>();
            TwitteController.likeAction = new ArrayList<>();
            TwitteController.commentList = new ArrayList<>();
            TwitteController.commentAction = new ArrayList<>();
            TwitteController.retwitteList = new ArrayList<>();
            TwitteController.retwitteAction = new ArrayList<>();
            TwitteController.Share = new ArrayList<>();
            TwitteController.Save = new ArrayList<>();

            TwitteController.twitteImages=new ArrayList<>();

            followerCount = (Button) launch.view.root.lookup("#follower_count");
            followingCount = (Button) launch.view.root.lookup("#following_count");
            blockCount = (Button) launch.view.root.lookup("#block_count");
            muteCount = (Button) launch.view.root.lookup("#mute_count");

            followAction = (Button) launch.view.root.lookup("#follow_action");
            blockAction = (Button) launch.view.root.lookup("#block_action");
            muteAction = (Button) launch.view.root.lookup("#mute_action");
            removeAction = (Button) launch.view.root.lookup("#remove_action");
            sendMessage = (Button) launch.view.root.lookup("#send_message");

            followsYou = (Label) launch.view.root.lookup("#follows_you");
            mutesYou = (Label) launch.view.root.lookup("#mutes_you");
            blocksYou = (Label) launch.view.root.lookup("#blocks_you");

            ImageView Icon = (ImageView) launch.view.root.lookup("#picture");
            try {
                try {
                    getImageFile getProfileFile = new getImageFile();
                    File file = getProfileFile.profile(anouther_username+"");
                    Image image = new Image(file.toURI().toString());
                    FileUtils.readFileToByteArray(file);
                    Icon.setImage(image);
                } catch (NullPointerException| FileNotFoundException ignored) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch (NullPointerException ignored){
            }


                Icon.setOnMouseClicked(mouseEvent -> {


                        try {
                            getImageFile getProfileFile = new getImageFile();
                            File file = getProfileFile.profile(anouther_username+"");
                            FileUtils.readFileToByteArray(file);
                            new selectIcon(anouther_username,2);
                        } catch (NullPointerException| FileNotFoundException ignored) {
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                });


            new userController(anouther_username,username);


            refreshList(username, anouther_username);
            resetCounts(anouther_username);
            buttonsSetter(username, anouther_username);

            menu.Menu_command(username);

            if (users.get(uf.UserFinder(username)).getIsEnable()) {


                followAction.setOnAction(event -> {
                    if (!(users.get(target).getBlocks().contains(username))) {
                        if (!users.get(target).getFollowers().contains(username)) {

                            if (users.get(target).getAccount()) {
                                submit.Submit_unblock(username, anouther_username, true);
                                submit.Submit_follow(username, anouther_username);
                                logger.info("System: " + anouther_username + " followed!");
                            } else {
                                if (req) {
                                    subreq.submitUnreq(username, anouther_username);
                                    logger.info("System: Request deleted");
                                } else {
                                    submit.Submit_unblock(username, anouther_username, true);
                                    subreq.submitReq(username, anouther_username);
                                    logger.info("System: Request sent");
                                }
                            }
                        } else {
                            submit.Submit_unfollow(username, anouther_username, true);
                            logger.info("System: " + anouther_username + " unfollowed!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "You have blocked already, you cant follow "
                                        + users.get(target).getName() + "!");

                    }

                    try {
                        new userProfile(username, anouther_username);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });


                blockAction.setOnAction(event -> {
                    if (!users.get(userFinder.UserFinder(username)).getBlocks().contains(anouther_username)) {
                        subreq.submitUnreq(username, anouther_username);
                        submit.Submit_unfollow(username, anouther_username, false);
                        submit.Submit_unfollow(anouther_username, username, false);
                        submit.Submit_block(username, anouther_username);
                    } else {
                        submit.Submit_unblock(username, anouther_username, true);
                    }
                    try {
                        new userProfile(username, anouther_username);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                muteAction.setOnAction(event -> {
                    if (!users.get(userFinder.UserFinder(username)).getMutes().contains(anouther_username)) {
                        submit.Submit_mute(username, anouther_username);
                        logger.info("System: " + anouther_username + " muted!");
                    } else {
                        submit.Submit_unmute(username, anouther_username);
                        logger.info("System: " + anouther_username + " unmuted!");
                    }
                    refreshList(username, anouther_username);
                    buttonsSetter(username, anouther_username);
                    resetCounts(anouther_username);
                });

                removeAction.setOnAction(event -> {
                    if (users.get(target).getFollowings().contains(username)) {
                        submit.Submit_delete(username, anouther_username);
                        logger.info("System: " + anouther_username + " deleted!");
                    }
                    refreshList(username, anouther_username);
                    buttonsSetter(username, anouther_username);
                    resetCounts(anouther_username);
                });

                sendMessage.setOnAction(event -> {

                        if (users.get(target).getFollowings().contains(username)) {
                            try {
                                new chatPage(username, anouther_username);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        refreshList(username, anouther_username);
                        buttonsSetter(username, anouther_username);
                        resetCounts(anouther_username);

                });
            }
            followingCount.setOnAction(event -> {
                if (users.get(target).getAccount() || users.get(target).getFollowers().contains(username)) {
                    if (users.get(target).getFollowings().size() == 1)
                        JOptionPane.showMessageDialog(null, (username + " dont follow anybody!"));
                    else
                        try {
                            new Lists(anouther_username, username, 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });
            followerCount.setOnAction(event -> {
                if (users.get(target).getFollowers().size() == 1)
                    JOptionPane.showMessageDialog(null, "Nobody follows " + username);
                else if (users.get(target).getAccount() || users.get(target).getFollowers().contains(username)) {
                    try {
                        new Lists(anouther_username, username, 2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            blockCount.setOnAction(event -> {
                if (users.get(target).getBlocks().size() == 1)
                    JOptionPane.showMessageDialog(null, (username + " dont block anybody"));
                else if (users.get(target).getAccount() || users.get(target).getFollowers().contains(username)) {
                    try {
                        new Lists(anouther_username, username, 3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            muteCount.setOnAction(event -> {
                if (users.get(target).getAccount() || users.get(target).getFollowers().contains(username)) {
                    if (users.get(target).getMutes().size() == 1)
                        JOptionPane.showMessageDialog(null, (username + " dont mute anybody"));
                    else
                        try {
                            new Lists(anouther_username, username, 4);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });

            if (users.get(target).getAccount() || users.get(target).getFollowers().contains(username))
                new userTwittes(anouther_username, username);
            else
                new userTwittes();
        }
    }

    public void buttonsSetter(String username, String anouther_username) {

        var get_j = new jsonUsers();
        List<objUsers> users = get_j.get();


        if (!users.get(target).getFollowers().contains(username)) {
            if (users.get(target).getAccount()) {
                followAction.setText("Follow");
                followAction.getStyleClass().remove("logined");
            } else {
                if (req) {
                    followAction.setText("UnRequest");
                    if (!followAction.getStyleClass().contains("logined"))
                        followAction.getStyleClass().add("logined");
                } else {
                    followAction.setText("Request");
                    followAction.getStyleClass().remove("logined");
                }
            }
        } else {
            blockAction.setText("Block");
            blockAction.getStyleClass().remove("logined");
            followAction.setText("Unfollow");
            if (!followAction.getStyleClass().contains("logined"))
                followAction.getStyleClass().add("logined");
        }

        if (users.get(userFinder.UserFinder(username)).getBlocks().contains(anouther_username)) {
            if (users.get(target).getAccount()) {
                followAction.setText("Follow");
                followAction.getStyleClass().remove("logined");
            } else {
                followAction.setText("Request");
                followAction.getStyleClass().remove("logined");
            }
            blockAction.setText("Unblock");
            if (!blockAction.getStyleClass().contains("logined"))
                blockAction.getStyleClass().add("logined");
        } else {
            blockAction.setText("Block");
            blockAction.getStyleClass().remove("logined");
        }


        if (users.get(userFinder.UserFinder(username)).getMutes().contains(anouther_username)) {
            muteAction.setText("Unmute");
            if (!muteAction.getStyleClass().contains("logined"))
                muteAction.getStyleClass().add("logined");
        } else {
            muteAction.setText("Mute");
            muteAction.getStyleClass().remove("logined");
        }

        if (!users.get(target).getFollowings().contains(username) && !anouther_username.equals(username))
            sendMessage.setVisible(false);
        if (!users.get(target).getFollowings().contains(username)) removeAction.setVisible(false);
        followsYou.setVisible(users.get(target).getFollowings().contains(username));
        mutesYou.setVisible(users.get(target).getBlocks().contains(username));
        blocksYou.setVisible(users.get(target).getMutes().contains(username));
    }
}



