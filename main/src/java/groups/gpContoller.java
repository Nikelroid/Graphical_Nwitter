package groups;

import graphics.selectIcon;
import imageControl.getImageFile;
import javafx.application.Platform;
import javafx.scene.image.Image;
import jsonContoller.jsonMessage;
import jsonContoller.jsonUsers;
import lists.userSelect;
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

public class gpContoller {
    public gpContoller() {

    }
    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages = get_mess.get();
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    userFinder uf = new userFinder();
    private static final Logger logger = LogManager.getLogger(gpContoller.class);
    protected gpContoller(String me, int serial){
        for (int i = 0; i < gpPrinter.Save.size(); i++) {
            final int finalI = i;
            if (users.get(uf.UserFinder(me)).getPmsaved().contains(messages.get(gpChat.mapper[finalI]).getSerial())) {
                gpPrinter.Save.get(i).getStyleClass().remove("share");
                gpPrinter.Save.get(i).getStyleClass().add("saved");
                gpPrinter.Save.get(i).setText("Unsave");
            }
            if (messages.get(gpChat.mapper[finalI]).getText().charAt(0)=='^') {
                gpPrinter.Edit.get(finalI).setVisible(false);
            }
            gpPrinter.Save.get(i).setOnAction(event -> {
                if (users.get(uf.UserFinder(me)).getPmsaved().contains(messages.get(gpChat.mapper[finalI]).getSerial())) {
                    int targetMessage = 0;
                    for (int j = 0; j < users.get(uf.UserFinder(me)).getPmsaved().size(); j++) {
                        if (users.get(uf.UserFinder(me)).getPmsaved().get(j) == messages.get(gpChat.mapper[finalI]).getSerial()) {
                            targetMessage = j;
                            break;
                        }
                    }
                    users.get(uf.UserFinder(me)).getPmsaved().remove(targetMessage);
                    new jsonUsers(users);
                    double vValue = gpPrinter.scrollPane.getVvalue();

                        gpPrinter.Save.get(finalI).getStyleClass().add("share");
                        gpPrinter.Save.get(finalI).getStyleClass().remove("saved");
                        gpPrinter.Save.get(finalI).setText("Save");

                } else {
                    users.get(uf.UserFinder(me)).getPmsaved().add(messages.get(gpChat.mapper[finalI]).getSerial());
                    new jsonUsers(users);

                    gpPrinter.Save.get(finalI).getStyleClass().remove("share");
                    gpPrinter.Save.get(finalI).getStyleClass().add("saved");
                    gpPrinter.Save.get(finalI).setText("Unsave");
                }
            });
            gpPrinter.Delete.get(i).setOnAction(event -> {
                int input = JOptionPane.showConfirmDialog(null, "" +
                        "Are you sure to delete this message ?");
                if (input == 0) {
                    messages.remove(gpChat.mapper[finalI]);
                    new jsonMessage(messages);
                }

                gpPrinter.chatList.getChildren().remove(finalI);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gpPrinter.scrollPane.setVvalue(1.0);
                    }
                });
            });

            gpPrinter.Forward.get(i).setOnAction(event -> {
                var uf = new userFinder();
                int target = uf.UserFinder(me);
                String gpSerial = messages.get(gpChat.mapper[finalI]).getReceiver().substring(1);
                int gpNum=0;
                for (int j = 0; j < users.get(target).getGroups().size(); j++) {
                    if(users.get(target).getGroups().get(j).get(0).equals(gpSerial))
                        gpNum=j;
                }
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
                            sb.SubMess("^Forwarded message from "
                                            + messages.get(gpChat.mapper[finalI]).getSender()+ " to "+
                                            users.get(target).getGroups().get(gpNum).get(1) + " group : "+
                                            messages.get(gpChat.mapper[finalI]).getText(),
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
            gpPrinter.Edit.get(i).setOnAction(event -> {

                gpChat.messageText.setText(messages.get(gpChat.mapper[finalI]).getText());
                gpChat.send.setText("Edit");
                gpChat.picture.setVisible(false);
                gpChat.send.setOnAction(event1 -> {
                    if (!gpChat.messageText.getText().isEmpty() &&
                            !gpChat.messageText.getText().equals(messages.get(gpChat.mapper[finalI]).getText())){
                        messages.get(gpChat.mapper[finalI]).setText(gpChat.messageText.getText());
                        new jsonMessage(messages);
                        try {
                            new gpChat(me,serial);
                        } catch (Exception e) {
                            logger.error("Error in reload page after edit");
                        }
                    }
                });
                launch.view.scene.setOnKeyPressed(keyEvent -> {
                    if (!gpChat.messageText.getText().isEmpty() &&
                            !gpChat.messageText.getText().equals(messages.get(gpChat.mapper[finalI]).getText())){
                        messages.get(gpChat.mapper[finalI]).setText(gpChat.messageText.getText());
                        new jsonMessage(messages);
                    }
                });
            });
            gpPrinter.messageImage.get(i).setOnMouseClicked(mouseEvent -> {
                try {
                    getImageFile getProfileFile = new getImageFile();
                    File file = getProfileFile.message(messages.get(gpChat.mapper[finalI]).getSerial() +"");
                    Image image = new Image(file.toURI().toString());
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
