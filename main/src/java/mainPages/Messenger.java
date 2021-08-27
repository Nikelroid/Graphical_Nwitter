package mainPages;

import chat.chatPage;
import chat.newChat;
import groups.gpMain;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonMessage;
import jsonContoller.jsonUsers;
import messengerChilds.massMessenger;
import messengerChilds.savedMessages;
import messengerChilds.savedTwittes;
import objects.objMessage;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitteControl.Menu;
import userControl.userFinder;
import userControl.usersPrint;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Messenger {
    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages = get_mess.get();
    ArrayList<String> retorder = new ArrayList<String>();
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();

    AnchorPane card;

    FXMLLoader fXMLLoader = new FXMLLoader();
    public static ArrayList<Button> actionButton;

    int target;
    int counter=1;
    int nonseen;
    int [] mapper = new int[10000];
    boolean [] marker = new boolean[10000];
    private static final Logger logger = LogManager.getLogger(Messenger.class);

    private void adder () {
        ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
        VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
        twitteList.getChildren().add(card);
    }

    public Messenger(String username) throws Exception {
        logger.info("System: user went to mainPages.Messenger");
        for (int j = 0; j < users.size(); j++)
            if (users.get(j).getUsername().equals(username))
                target=j;
        retorder = new ArrayList<String>();
        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/general_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.setTitle("Messenger");
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
        Header.setText("Messenger");
        actionButton = new ArrayList<>();
        var menu = new Menu();
        menu.Menu_command(username);

        card = FXMLLoader.load(getClass().getResource("/layout/header/messenger_header.fxml"));
        Button New = (Button) card.lookup("#new");
        Button Saved = (Button) card.lookup("#saved");
        Button Mass = (Button) card.lookup("#mass");
        Button savedT = (Button) card.lookup("#saved_twittes");
        Button Gp = (Button) card.lookup("#gp");
        adder();
        New.setOnAction(event -> {
            try {
                new newChat(username);
            } catch (Exception e) {
                logger.error("Error in open new chat");
            }
        });
        Saved.setOnAction(event -> {
            if (users.get(target).getPmsaved().size()<2)
                JOptionPane.showMessageDialog(null,"You didn't save any message yet!");
            else
            try {
                new savedMessages(username);
            } catch (Exception e) {
                e.printStackTrace();
               logger.error("Error in open saved message");
            }
        });
        Gp.setOnAction(event -> {
                try {
                    new gpMain(username);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Error in open Group message");
                }
        });
        savedT.setOnAction(event -> {
            if (users.get(target).getTwittesaved().size()<2)
                JOptionPane.showMessageDialog(null,"You didn't save any message twittes!");
            else
                try {
                    new savedTwittes(username);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Error in open saved twittes");
                }
        });
        Mass.setOnAction(event -> {
            try {
                new massMessenger(username);
            } catch (Exception e) {
                logger.error("Error in open mass messenger");
            }
        });


        userControl.usersPrint.Delete=new ArrayList<>();
        userControl.usersPrint.user = new ArrayList<>();


        for (int i = messages.size()-1; i>=0 ; i--) {
            if (messages.get(i).getReceiver().equals(username)
                    && !retorder.contains(messages.get(i).getSender()))
                for (int j = 0; j < users.size(); j++)
                    if ((users.get(j).getUsername().equals(messages.get(i).getReceiver()) && messages.get(i).getSender().equals(username))
                            || (users.get(j).getUsername().equals(messages.get(i).getSender()) && messages.get(i).getReceiver().equals(username)))
                        if (!users.get(j).getBlocks().contains(username) &&
                                !users.get(target).getBlocks().contains(users.get(j).getUsername())
                                && !(users.get(j).getUsername().equals(username)) && users.get(j).getIsEnable()) {

                            nonseen = 0;
                            for (int k = messages.size() - 1; k >= 0; k--) {
                                if (messages.get(k).getReceiver().equals(username) &&
                                        messages.get(k).getSender().equals(messages.get(i).getSender()) &&
                                        !messages.get(k).isSeen()) nonseen++;
                            }

                            if (nonseen != 0) {
                                new usersPrint(users.get(j).getUsername(), nonseen,counter);
                                retorder.add(users.get(j).getUsername());
                                mapper[counter - 1] = j;
                                marker[counter - 1] = false;
                                counter++;
                            }
                        }
        }


        for (int i = messages.size()-1; i>=0 ; i--) {
            if (!retorder.contains(messages.get(i).getReceiver()) &&
                    !retorder.contains(messages.get(i).getSender()))
                for (int j = 0; j < users.size(); j++)
                    if ((users.get(j).getUsername().equals(messages.get(i).getReceiver()) && messages.get(i).getSender().equals(username))
                    || (users.get(j).getUsername().equals(messages.get(i).getSender()) && messages.get(i).getReceiver().equals(username))) {
                        if (!users.get(j).getBlocks().contains(username) &&
                                !users.get(target).getBlocks().contains(users.get(j).getUsername())
                                && !(users.get(j).getUsername().equals(username)) && users.get(j).getIsEnable()) {

                            new usersPrint(users.get(j).getUsername(), 0, counter);
                            retorder.add(users.get(j).getUsername());
                            mapper[counter - 1] = j;
                            marker[counter - 1] = true;
                            counter++;
                        }
                    }
        }

        for (int i = 0; i < usersPrint.Delete.size(); i++) {
            final int finalI=i;
            usersPrint.Delete.get(i).setOnAction(event -> {
            logger.info("User wants delete a chat");
                int input = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want delete your chat with " +
                                users.get(mapper[finalI]).getName()+
                        " ?");
                if (input==0){
                    for (int k = 0; k < messages.size(); k++) {
                    for (int j = 0; j < messages.size(); j++) {
                        if (messages.get(j).getSender().equals(users.get(mapper[finalI]).getUsername())
                                && messages.get(j).getReceiver().equals(username)
                                || messages.get(j).getReceiver().equals(users.get(mapper[finalI]).getUsername())
                                && messages.get(j).getSender().equals(username)) {
                            messages.remove(j);
                            k=0;
                            break;
                        }
                    }
                    }
                    new jsonMessage(messages);
                    try {
                        new Messenger(username);
                    } catch (Exception e) {
                        logger.error("error in reload messenger");
                    }
                }
        });
            usersPrint.user.get(i).setOnMouseClicked(mouseEvent -> {
                try {
                    new chatPage(username,users.get(mapper[finalI]).getUsername());
                } catch (Exception e) {
                   e.printStackTrace();
                }
            });

            usersPrint.user.get(i).setOnMouseEntered(mouseEvent -> {
                launch.view.scene.setCursor(Cursor.HAND);
                usersPrint.user.get(finalI).getStyleClass().remove("username");
                usersPrint.user.get(finalI).getStyleClass().add("focused");
            });
            usersPrint.user.get(i).setOnMouseExited(mouseEvent -> {
                launch.view.scene.setCursor(Cursor.DEFAULT);
                usersPrint.user.get(finalI).getStyleClass().add("username");
                usersPrint.user.get(finalI).getStyleClass().remove("focused");
            });
    }




        }
}
