package chat;

import graphics.selectIcon;
import imageControl.getImageFile;
import javafx.application.Platform;
import javafx.scene.image.Image;
import jsonContoller.jsonMessage;
import jsonContoller.jsonUsers;
import lists.userSelect;
import messengerChilds.savedMessages;
import objects.objMessage;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class chatContoller {
    public chatContoller() {

    }
    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages = get_mess.get();
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    userFinder uf = new userFinder();
    private static final Logger logger = LogManager.getLogger(chatContoller.class);
    public chatContoller(String me, String he){
        for (int i = 0; i < chatPrinter.Save.size(); i++) {
            final int finalI = i;
            if (users.get(uf.UserFinder(me)).getPmsaved().contains(messages.get(chatPage.mapper[finalI]).getSerial())) {
                chatPrinter.Save.get(i).getStyleClass().remove("share");
                chatPrinter.Save.get(i).getStyleClass().add("saved");
                chatPrinter.Save.get(i).setText("Unsave");
            }
            if (messages.get(chatPage.mapper[finalI]).getText().charAt(0)=='^') {
                chatPrinter.Edit.get(finalI).setVisible(false);
            }
            chatPrinter.Save.get(i).setOnAction(event -> {
                if (users.get(uf.UserFinder(me)).getPmsaved().contains(messages.get(chatPage.mapper[finalI]).getSerial())) {
                    int targetMessage = 0;
                    for (int j = 0; j < users.get(uf.UserFinder(me)).getPmsaved().size(); j++) {
                        if (users.get(uf.UserFinder(me)).getPmsaved().get(j) == messages.get(chatPage.mapper[finalI]).getSerial()) {
                            targetMessage = j;
                            break;
                        }
                    }
                    users.get(uf.UserFinder(me)).getPmsaved().remove(targetMessage);
                    new jsonUsers(users);
                    double vValue = chatPrinter.scrollPane.getVvalue();
                    if (me.equals(he)){
                        try {
                            new savedMessages(me,vValue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        chatPrinter.Save.get(finalI).getStyleClass().add("share");
                        chatPrinter.Save.get(finalI).getStyleClass().remove("saved");
                        chatPrinter.Save.get(finalI).setText("Save");
                    }
                } else {
                    users.get(uf.UserFinder(me)).getPmsaved().add(messages.get(chatPage.mapper[finalI]).getSerial());
                    new jsonUsers(users);

                    chatPrinter.Save.get(finalI).getStyleClass().remove("share");
                    chatPrinter.Save.get(finalI).getStyleClass().add("saved");
                    chatPrinter.Save.get(finalI).setText("Unsave");
                }
            });
            chatPrinter.Delete.get(i).setOnAction(event -> {
                int input = JOptionPane.showConfirmDialog(null, "" +
                        "Are you sure to delete this message ?");
                if (input == 0) {
                    messages.remove(chatPage.mapper[finalI]);
                    new jsonMessage(messages);
                }
                if (me.equals(he)){
                    int targetMessage=0;
                    for (int j = 0; j < users.get(uf.UserFinder(me)).getPmsaved().size(); j++) {
                        if (users.get(uf.UserFinder(me)).getPmsaved().get(j) == messages.get(chatPage.mapper[finalI]).getSerial()) {
                            targetMessage = j;
                            break;
                        }
                    }
                    users.get(uf.UserFinder(me)).getPmsaved().remove(targetMessage);
                }
                chatPrinter.chatList.getChildren().remove(finalI);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        chatPrinter.scrollPane.setVvalue(1.0);
                    }
                });
            });

            chatPrinter.Forward.get(i).setOnAction(event -> {
                var uf = new userFinder();
                int target = uf.UserFinder(me);
                if (users.get(target).getFollowings().size()>1) {
                    userSelect.select = new ArrayList<>();
                    try {
                        new userSelect(me);
                    } catch (IOException e) {
                        logger.error("Error in loading user select page from mass messenger");
                    }
                    boolean sent = false;
                    for (int z = 0; z < userSelect.select.size(); z++) {
                        if (userSelect.select.get(z).isSelected() &&
                                !users.get(target).getFollowings().get(z).equals(me)){
                            var sb = new submitMessage();
                            sb.SubMess("^Forwarded message for "
                                    + messages.get(chatPage.mapper[finalI]).getSender()+ " to "+
                                    messages.get(chatPage.mapper[finalI]).getReceiver() + " : "+
                                    messages.get(chatPage.mapper[finalI]).getText(),
                                    me,
                                    users.get(target).getFollowings().get(z+1));
                            sent = true;
                        }
                    }
                    if (sent) {
                        JOptionPane.showMessageDialog(null, "Messages sent to selected followings!");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "You dont follow anybody");
                }
            });
            chatPrinter.Edit.get(i).setOnAction(event -> {

                chatPage.messageText.setText(messages.get(chatPage.mapper[finalI]).getText());
                chatPage.send.setText("Edit");
                chatPage.picture.setVisible(false);
                chatPage.send.setOnAction(event1 -> {
                    if (!chatPage.messageText.getText().isEmpty() &&
                            !chatPage.messageText.getText().equals(messages.get(chatPage.mapper[finalI]).getText())){
                        messages.get(chatPage.mapper[finalI]).setText(chatPage.messageText.getText());
                        new jsonMessage(messages);
                        try {
                            new chatPage(me,he);
                        } catch (Exception e) {
                            logger.error("Error in reload page after edit");
                        }
                    }
                });
                launch.view.scene.setOnKeyPressed(keyEvent -> {
                    if (!chatPage.messageText.getText().isEmpty() &&
                            !chatPage.messageText.getText().equals(messages.get(chatPage.mapper[finalI]).getText())){
                        messages.get(chatPage.mapper[finalI]).setText(chatPage.messageText.getText());
                        new jsonMessage(messages);
                        try {
                            new chatPage(me,he);
                        } catch (Exception e) {
                            logger.error("Error in reload page after edit");
                        }
                    }
                });
            });

            chatPrinter.messageImage.get(i).setOnMouseClicked(mouseEvent -> {
                try {
                    getImageFile getProfileFile = new getImageFile();
                    File file = getProfileFile.message(
                            messages.get(chatPage.mapper[finalI]).getSerial()+"");
                    FileUtils.readFileToByteArray(file);
                    try {
                        new selectIcon(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (NullPointerException| FileNotFoundException ignored) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }

    }
}
