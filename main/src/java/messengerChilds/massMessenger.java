package messengerChilds;

import category.Category;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import jsonContoller.jsonMessage;
import jsonContoller.jsonUsers;
import lists.userSelect;
import mainPages.Messenger;
import objects.objMessage;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitMessage;
import userControl.userFinder;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class massMessenger {


    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();

    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages = get_mess.get();
    submitMessage sb = new submitMessage();
    int target,sounter=1;
    int counter=1;
    int[] mapper = new int [10000];
    int[] chater = new int [10000];
    public static int catNum=-1;
    public static Button Back;
    private static final Logger logger = LogManager.getLogger(massMessenger.class);
    public massMessenger(String username,int catNum) throws Exception {
        massMessenger.catNum=catNum;
        new massMessenger(username);
    }
    public massMessenger(String username) throws Exception {
        logger.info("System: user went to messengerChilds.massMessenger");

        var uf = new userFinder();
        target = uf.UserFinder(username);

        Parent ListRoot = FXMLLoader.load(getClass().getResource("/layout/mini_page/mass_message.fxml"));
        Scene ListScene = new Scene(ListRoot);
        Stage ListStage = new Stage();
        ListStage.setScene(ListScene);
        ListStage.show();
        TextArea Text = (TextArea) ListScene.lookup("#text");
        Back = (Button) ListScene.lookup("#back");
        Button All = (Button) ListScene.lookup("#all");
        Button Cat = (Button) ListScene.lookup("#cat");
        Button Selected = (Button) ListScene.lookup("#selected");
        Button Pic = (Button) ListScene.lookup("#pic");
        Back.setText("Cancel");
        if(catNum!=-1) {
            All.setVisible(false);
            Selected.setVisible(false);
            Cat.setMaxWidth(Region.USE_COMPUTED_SIZE);
            Cat.setMinWidth(Region.USE_COMPUTED_SIZE);
            Cat.setPrefWidth(Region.USE_COMPUTED_SIZE);
            Cat.setText("Send to "+ users.get(target).getCategoiries().get(catNum).get(0) +" category");
        }
        All.setOnAction(event -> {
            if (!Text.getText().isEmpty()) {
                if (users.get(target).getFollowings().size()>1) {
                    for (int j = 1; j < users.get(target).getFollowings().size(); j++) {
                        sb.SubMess(Text.getText(), username, users.get(target).getFollowings().get(j));
                    }
                    JOptionPane.showMessageDialog(null, "Messages sent to all followings!");
                    try {
                        Back.getScene().getWindow().hide();
                        new Messenger(username);
                    } catch (Exception e) {
                        logger.error("Error in go to message after message to all");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "You dont follow anybody to send message");
                }
            }
        });

        Selected.setOnAction(event -> {
            if (!Text.getText().isEmpty()) {
                if (users.get(target).getFollowings().size()>1) {
                    userSelect.select = new ArrayList<>();
                    try {
                        new userSelect(username);
                    } catch (IOException e) {
                        logger.error("Error in loading user select page from mass messenger");
                    }
                    boolean sent = false;
                    for (int i = 0; i < userSelect.select.size(); i++) {
                        if (userSelect.select.get(i).isSelected() &&
                                !users.get(target).getFollowings().get(i).equals(username)) {
                            sb.SubMess(Text.getText(), username, users.get(target).getFollowings().get(i+1));
                            sent = true;
                        }
                    }
                    if (sent)
                    JOptionPane.showMessageDialog(null, "Messages sent to selected followings!");

                    try {
                        Back.getScene().getWindow().hide();
                        new Messenger(username);
                    } catch (Exception e) {
                        logger.error("Error in go to message after message to all");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "You dont follow anybody");

                }
            }
        });

        ListScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER&& !Text.getText().isEmpty()  &&  catNum!=-1 ) {

                for (int j = 1; j < users.get(target).getCategoiries().get(catNum).size(); j++) {
                    var sb = new submitMessage();
                    sb.SubMess(Text.getText(), username, users.get(target).
                            getCategoiries().get(catNum).get(j));
                }
                JOptionPane.showMessageDialog(null,"Message sent to members of "+
                        users.get(target).getCategoiries().get(catNum).get(0) +" category");
                try {
                    catNum=-1;
                    Back.getScene().getWindow().hide();
                    new Category(username);
                } catch (Exception e) {
                    logger.error("error in go to category after sending message to category");
                }
            }
        });
        Cat.setOnAction(event -> {
            if (!Text.getText().isEmpty())
                if (users.get(target).getCategoiries().size()>1) {
                    if (catNum == -1) {
                        try {
                            new Category(username, Text.getText());
                        } catch (Exception e) {
                            logger.error("Error in go to category for sending message");
                        }
                    } else {
                        for (int j = 1; j < users.get(target).getCategoiries().get(catNum).size(); j++) {
                            var sb = new submitMessage();
                            sb.SubMess(Text.getText(), username, users.get(target).
                                    getCategoiries().get(catNum).get(j));
                        }
                        JOptionPane.showMessageDialog(null, "Message sent to members of " +
                                users.get(target).getCategoiries().get(catNum).get(0) + " category");
                        try {
                            catNum = -1;
                            Back.getScene().getWindow().hide();
                            new Category(username);
                        } catch (Exception e) {
                            logger.error("error in reloading mass messenger after sending message");
                        }
                    }
                }else{
            JOptionPane.showMessageDialog(null, "You dont have any categories");

        }
        });

        Back.setOnAction(event -> {
            Back.getScene().getWindow().hide();
        });

    }
}
