package messengerChilds;

import chat.chatContoller;
import chat.chatPage;
import chat.chatPrinter;
import graphics.selectIcon;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import jsonContoller.jsonMessage;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import mainPages.Info;
import mainPages.Messenger;
import objects.objMessage;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitMessage;
import userControl.userFinder;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class savedMessages {
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages = get_mess.get();
    submitMessage sb = new submitMessage();
    int counter = 0;
    int target;
    public static double vValue = -1;
    private static final Logger logger = LogManager.getLogger(savedMessages.class);
    public savedMessages(String username,double vValue) throws Exception {
        savedMessages.vValue =vValue;
        new savedMessages(username);

    }
    public savedMessages(String username) throws Exception {
        logger.info("System: user went to messengerChilds.savedMessages");
        chatPage.mapper = new int[10000];
        var uf = new userFinder();
        target = uf.UserFinder(username);

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/chat_page.fxml"));
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
                users.get(uf.UserFinder(username)).setLastseen(dtf.format(now));
                new jsonUsers(users);
                System.exit(1);
            }
        });

        Label Header = (Label) launch.view.scene.lookup("#header");
        Header.setText("Saved Messages");
        Button Back = (Button) launch.view.scene.lookup("#back");
        Back.setText("Back");
        chatPrinter.Forward=new ArrayList<>();
        chatPrinter.Save=new ArrayList<>();
        chatPrinter.Delete=new ArrayList<>();
        chatPrinter.Edit=new ArrayList<>();

        chatPrinter.messageImage=new ArrayList<>();

        chatPrinter.scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
        chatPrinter.chatList = (VBox) chatPrinter.scrollPane.lookup("#twittelist");


                for (int i = 0; i < users.get(target).getPmsaved().size(); i++)
                    for (int j = 0; j < messages.size(); j++)
                        if (users.get(target).getPmsaved().get(i).equals(messages.get(j).getSerial()))
                            if (messages.get(j).getSender().equals(username)) {
                                new chatPrinter(messages.get(j).getSerial(), 1);
                                chatPrinter.Seen.setVisible(false);
                                chatPage.mapper[counter]=j;
                                counter++;
                            }else {
                                chatPage.mapper[counter]=j;
                                counter++;
                                new chatPrinter(messages.get(j).getSerial(), 2);
                            }

        chatPage.messageText = (TextArea) launch.view.scene.lookup("#message_text");
        chatPage.send = (Button) launch.view.scene.lookup("#send");
        chatPage.picture = (Button) launch.view.scene.lookup("#pic");

        chatPage.picture.setOnAction(event -> {
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

        chatPage.send.setOnAction(event -> {
            if (!chatPage.messageText.getText().isEmpty()) {
                int savedSerial = sb.SubMess(chatPage.messageText.getText(), username, username);
                users.get(target).getPmsaved().add(savedSerial);
                new jsonUsers(users);
            }


                if (selectIcon.ImageIm != null) {
                    if (chatPage.messageText.getText().isEmpty()) {
                        int savedSerial = sb.SubMess(" ", username, username);
                        users.get(target).getPmsaved().add(savedSerial);
                        new jsonUsers(users);
                    }
                    messages = get_mess.get();
                    try {
                        Path simple = Paths.get("simple.png");
                        Path path = Paths.get(simple.toAbsolutePath().getParent() +
                                "\\main\\src\\resources\\messages\\" +
                                (messages.get(messages.size() - 1).getSerial()) + ".png");
                        Files.copy(selectIcon.fileIm.toPath(), path);

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    selectIcon.fileIm = null;
                    selectIcon.ImageIm = null;
                }
            try {
                new savedMessages(username);
            } catch (Exception e) {
                logger.error("Error in realoding chat page");
            }
        });

        launch.view.scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                if (!chatPage.messageText.getText().isEmpty()) {
                    int savedSerial = sb.SubMess(chatPage.messageText.getText(), username, username);
                    users.get(target).getPmsaved().add(savedSerial);
                    new jsonUsers(users);
                    try {
                        new savedMessages(username);
                    } catch (Exception e) {
                        logger.error("Error in reloading chat page");
                    }
                }
            }
        });

        Back.setOnAction(event -> {
            if (users.get(uf.UserFinder(username)).getIsEnable()) {
                try {
                    new Messenger(username);
                } catch (Exception e) {
                    logger.error("Error in back to chat list page");
                }
            }else{
                try {
                    new Info(username);
                } catch (Exception e) {
                    logger.error("Error in back to chat home page");
                }
            }

        });
        new chatContoller(username,username);
        if (savedMessages.vValue == -1) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatPrinter.scrollPane.setVvalue(1.0);
                }
            });
        }else{

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatPrinter.scrollPane.setVvalue(savedMessages.vValue);
                }
            });
        }
    }
}
