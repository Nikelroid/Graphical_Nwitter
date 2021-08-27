package chat;

import graphics.selectIcon;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import jsonContoller.jsonMessage;
import jsonContoller.jsonUsers;
import mainPages.Messenger;
import messengerChilds.savedMessages;
import objects.objMessage;
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

public class chatPage {
    chatContoller chatContoller = new chatContoller();
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages = get_mess.get();
    submitMessage sb = new submitMessage();
    userFinder uf = new userFinder();
    public static TextArea messageText;
    public static Button send,picture;
    int counter = 0;
    public static int[] mapper = new int[10000];
    private static final Logger logger = LogManager.getLogger(chatPage.class);

    public chatPage(String me, String he) throws Exception {
        if (me.equals(he))
            new savedMessages(me);
        else {
            logger.info("System: user went to ChatPage");

            for (int i = messages.size() - 1; i >= 0; i--)
                if (messages.get(i).getSender().equals(he) &&
                        messages.get(i).getReceiver().equals(me))
                    messages.get(i).setSeen(true);
            new jsonMessage(messages);
            messages = get_mess.get();
            chatPage.mapper = new int[10000];
            launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/chat_page.fxml"));
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
                    users.get(uf.UserFinder(me)).setLastseen(dtf.format(now));
                    new jsonUsers(users);
                    System.exit(1);
                }
            });

            Button Back = (Button) launch.view.scene.lookup("#back");
            Back.setText("Back");

            int target = uf.UserFinder(he);
            Label Header = (Label) launch.view.scene.lookup("#header");
            Header.setText(users.get(target).getName());

            chatPrinter.Forward = new ArrayList<>();
            chatPrinter.Save = new ArrayList<>();
            chatPrinter.Delete = new ArrayList<>();
            chatPrinter.Edit = new ArrayList<>();
            chatPrinter.messageImage = new ArrayList<>();

            chatPrinter.scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
            chatPrinter.chatList = (VBox) chatPrinter.scrollPane.lookup("#twittelist");

            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).getSender().equals(me) && messages.get(i).getReceiver().equals(he)) {
                    new chatPrinter(messages.get(i).serial, 1);
                    mapper[counter] = i;
                    counter++;
                }
                if (messages.get(i).getSender().equals(he) && messages.get(i).getReceiver().equals(me)) {
                    new chatPrinter(messages.get(i).serial, 2);
                    mapper[counter] = i;
                    counter++;
                }
            }

            messageText = (TextArea) launch.view.scene.lookup("#message_text");
            send = (Button) launch.view.scene.lookup("#send");
            picture = (Button) launch.view.scene.lookup("#pic");


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
                if (!messageText.getText().isEmpty()) {
                    sb.SubMess(messageText.getText(), me, he);
                }

                    if (selectIcon.ImageIm != null) {
                        if (messageText.getText().isEmpty()) {
                            try {
                                sb.SubMess(" ", me, he);
                            } catch (Exception e) {
                                logger.error("Error in creat a message in pv");
                            }
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
                    new chatPage(me, he);
                } catch (Exception e) {
                    logger.error("Error in realoding chat page");
                }
            });


            launch.view.scene.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    if (!messageText.getText().isEmpty()) {
                        sb.SubMess(messageText.getText(), me, he);
                        try {
                            new chatPage(me, he);
                        } catch (Exception e) {
                            logger.error("Error in realoding chat page");
                        }
                    }
                }
            });

            Back.setOnAction(event -> {
                try {
                    new Messenger(me);
                } catch (Exception e) {
                    logger.error("Error in back to chat list page");
                }
            });
            new chatContoller(me, he);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatPrinter.scrollPane.setVvalue(1.0);
                }
            });
        }
    }
}
