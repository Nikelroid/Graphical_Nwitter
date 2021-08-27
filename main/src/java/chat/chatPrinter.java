package chat;

import imageControl.getImageFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonMessage;
import jsonContoller.jsonUsers;
import objects.objMessage;
import objects.objUsers;
import org.apache.commons.io.FileUtils;
import userControl.userFinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class chatPrinter {
    public static ScrollPane scrollPane ;
    public static VBox chatList ;
    public static ArrayList<ImageView> messageImage;
    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages = get_mess.get();
    AnchorPane Message;
    Label Text , Name,Date;
    public static Label Seen;

    jsonUsers getUsers = new jsonUsers();
    List<objUsers> users = getUsers.get();

    public static ArrayList<Button> Forward, Save, Delete,commentAction, Edit, Share;

    private void difinerSender(int target) throws IOException {

        Message = FXMLLoader.load(getClass().getResource("/layout/chat/chat_sender.fxml"));
        Text = (Label) Message.lookup("#text");
        Name = (Label) Message.lookup("#name");
        Date = (Label) Message.lookup("#date");
        Seen = (Label) Message.lookup("#seen");
        iconDefiner(target);
        Edit.add((Button) Message.lookup("#edit"));
    }

    private void iconDefiner(int target) {
        ImageView Icon = (ImageView) Message.lookup("#icon");

        try {
            getImageFile getProfileFile = new getImageFile();
            File file = getProfileFile.message(messages.get(target).getSender()+"");
            Image image = new Image(file.toURI().toString());
            FileUtils.readFileToByteArray(file);
            Icon.setImage(image);
        } catch (NullPointerException| FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }


        messageImage.add((ImageView) Message.lookup("#image"));
        try {
            try {
                getImageFile getProfileFile = new getImageFile();
                File file = getProfileFile.message(messages.get(target).getSerial()+"");
                Image image = new Image(file.toURI().toString());
                FileUtils.readFileToByteArray(file);
                messageImage.get(messageImage.size()-1).setImage(image);
            } catch (NullPointerException| FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (NullPointerException ignored){
            messageImage.get(messageImage.size()-1).setVisible(false);
        }

        Forward.add((Button) Message.lookup("#forward"));
        Save.add((Button) Message.lookup("#save"));
        Delete.add((Button) Message.lookup("#delete"));
    }

    private void difinerReciever(int target) throws IOException {

        Message = FXMLLoader.load(getClass().getResource("/layout/chat/chat_reciver.fxml"));
        Text = (Label) Message.lookup("#text");
        Name = (Label) Message.lookup("#name");
        Date = (Label) Message.lookup("#date");
        iconDefiner(target);
        Edit.add(new Button());
    }

    private void setterSender(int target) {
        checkForward(target);
        if(messages.get(target).isSeen()) {
            Seen.setText("Seen");
            Seen.getStyleClass().remove("wrong");
            Seen.getStyleClass().add("accept");
        }
    }
    private void setterReciever(int target) {

        checkForward(target);
    }

    private void checkForward(int target) {
        forward(target, messages, Text, Date, Name);
    }

    public static void forward(int target, List<objMessage> messages, Label text, Label date, Label name) {

        if(!messages.get(target).getText().isEmpty() && messages.get(target).getText().charAt(0)=='^') {
            String modified = messages.get(target).getText().substring(1);
            text.setText(modified);
        }else {
            text.setText(messages.get(target).getText());
        }
        date.setText(" Date: "+ messages.get(target).getTime());
        name.setText(messages.get(target).getSender());
    }

    private void adder(){
        chatList.getChildren().add(Message);
    }
    public chatPrinter(int serial,int type) throws IOException {
        var uf = new userFinder();
        int target = uf.messageFinder(serial);

            if (type ==1 ) {
                difinerSender(target);
                setterSender(target);
            }else {
                difinerReciever(target);
                setterReciever(target);
            }
            adder();

    }
}
