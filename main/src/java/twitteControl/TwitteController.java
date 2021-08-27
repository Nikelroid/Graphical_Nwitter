package twitteControl;

import groups.gpPrinter;
import imageControl.getImageFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import mainPages.Feed;
import objects.objTwitte;
import objects.objUsers;
import org.apache.commons.io.FileUtils;
import submit.submitComment;
import userControl.userFinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class TwitteController {


    AnchorPane Twitte;
    FXMLLoader fXMLLoader = new FXMLLoader();
    Label Text ,Sender,Date;

    public static ArrayList<Button> likeList,likeAction,commentList,commentAction,retwitteList,
    retwitteAction, Share,Save,Report;

    public static ArrayList<Button> cLikeList,cLikeAction,cCommentList,cCommentAction,cRetwitteList,
            cRetwitteAction, cShare,cSave,cReport;
    AnchorPane Comments;
    Label cText ,cSender,cDate;
    public static ArrayList<ImageView> twitteImages;

    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    jsonUsers us = new jsonUsers();

    public TwitteController() throws IOException {
        Twitte = FXMLLoader.load(getClass().getResource("/layout/cards/Notwittes.fxml"));
        adder();
    }
    public TwitteController(int k) throws IOException {
        if (k==-1) {
            Twitte = FXMLLoader.load(getClass().getResource("/layout/cards/Notwittes.fxml"));
            Label text = (Label) Twitte.lookup("#twitte_text");
            ImageView image = (ImageView) Twitte.lookup("#image");
            text.setText("User's account is private");
            image.setImage(new Image(getClass().getResourceAsStream("/img/lock.png")));
            adder();
        }
    }

    private void difiner() throws IOException {

        Twitte = FXMLLoader.load(getClass().getResource("/layout/cards/twitte.fxml"));
        Text = (Label) Twitte.lookup("#twitte_text");
        Sender = (Label) Twitte.lookup("#sender");
        Date = (Label) Twitte.lookup("#date");
        likeList.add((Button) Twitte.lookup("#like_l"));
        likeAction.add((Button) Twitte.lookup("#like_a"));
        commentList.add((Button) Twitte.lookup("#comment_l"));
        commentAction.add((Button) Twitte.lookup("#comment_a"));
        retwitteList.add((Button) Twitte.lookup("#ret_l"));
        retwitteAction.add((Button) Twitte.lookup("#ret_a"));
        Share.add((Button) Twitte.lookup("#share"));
        Save.add((Button) Twitte.lookup("#save"));
        Report.add((Button) Twitte.lookup("#report"));

        twitteImages.add((ImageView)  Twitte.lookup("#image"));
    }

    private void setter(int counter,int i) {
        Text.setText(twittes.get(i).getText());
        Date.setText(" Date: "+twittes.get(i).getTime() + " ");
        likeList.get(counter-1).setText(String.valueOf(twittes.get(i).getLikes().size() - 1));
        commentList.get(counter-1).setText(String.valueOf(twittes.get(i).getComments().size() - 1));
        retwitteList.get(counter-1).setText(String.valueOf(twittes.get(i).getRetwittes().size() - 1));
        try {
            try {
                getImageFile getProfileFile = new getImageFile();
                File file = getProfileFile.twitte(twittes.get(i).getSerial()+"");
                Image image = new Image(file.toURI().toString());
                FileUtils.readFileToByteArray(file);
                twitteImages.get(counter-1).setImage(image);
            } catch (NullPointerException| FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (NullPointerException ignored){
            twitteImages.get(counter-1).setVisible(false);
        }

        Menu menu = new Menu();
        menu.buttonsStyleSetter(counter,i);
    }
    private void adder(){

        ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
        VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
        twitteList.getChildren().add(Twitte);
    }

    public TwitteController(int counter,int i) throws IOException {
        difiner();
        setter(counter,i);
        Sender.setText(" From "+twittes.get(i).getSender()+" : ");
        adder();
    }
    public TwitteController(String comment,int counter,int i) throws IOException {

        Comments = FXMLLoader.load(getClass().getResource("/layout/cards/twitte.fxml"));
        cText = (Label) Comments.lookup("#twitte_text");
        cSender = (Label) Comments.lookup("#sender");
        cDate = (Label) Comments.lookup("#date");
        cLikeList.add((Button) Comments.lookup("#like_l"));
        cLikeAction.add((Button) Comments.lookup("#like_a"));
        cCommentList.add((Button) Comments.lookup("#comment_l"));
        cCommentAction.add((Button) Comments.lookup("#comment_a"));
        cRetwitteList.add((Button) Comments.lookup("#ret_l"));
        cRetwitteAction.add((Button) Comments.lookup("#ret_a"));
        cShare.add((Button) Comments.lookup("#share"));
        cSave.add((Button) Comments.lookup("#save"));
        cReport.add((Button) Comments.lookup("#report"));

        cText.setText(twittes.get(i).getText());
        cDate.setText(" Date: "+twittes.get(i).getTime() + " ");
        cLikeList.get(counter-1).setText(String.valueOf(twittes.get(i).getLikes().size() - 1));
        cCommentList.get(counter-1).setText(String.valueOf(twittes.get(i).getComments().size() - 1));
        cRetwitteList.get(counter-1).setText(String.valueOf(twittes.get(i).getRetwittes().size() - 1));

        jsonUsers Users_get = new jsonUsers();
        List<objUsers> users = Users_get.get();
        if (twittes.get(i).getLikes().contains(Feed.username)) {

            TwitteController.cLikeAction.get(counter-1).getStyleClass().add("liked");
            TwitteController.cLikeAction.get(counter-1).setText("Unlike");
        }
        if(twittes.get(i).getRetwittes().contains(Feed.username)){
            TwitteController.cRetwitteAction.get(counter-1).getStyleClass().add("retwitted");
            TwitteController.cRetwitteAction.get(counter-1).setText("Retwitted");
        }
        userFinder us = new userFinder();
        int target = us.UserFinder(Feed.username);
        if(users.get(target).getTwittesaved().contains(twittes.get(i).getSerial())){
            TwitteController.cSave.get(counter-1).getStyleClass().add("saved");
            TwitteController.cSave.get(counter-1).setText("Romove");
        }

        cSender.setText(" From "+twittes.get(i).getSender()+" : ");
        ScrollPane cScrollPane = (ScrollPane) submitComment.cScene.lookup("#scobar");
        VBox CTwitteList = (VBox) cScrollPane.lookup("#twittelist");
        CTwitteList.getChildren().add(Comments);

    }

    public TwitteController(int counter,int i,String reter) throws IOException {
        difiner();
        setter(counter,i);
        Sender.setText(" "+reter + " Retwitted a nwitte From "+twittes.get(i).getSender()+" : ");
        adder();
    }

    public TwitteController(int counter,int i,int j) throws IOException {
        difiner();
        setter(counter,i);
        Sender.setText(" This is a comment of "+twittes.get(i).getSender()+
                " for "+twittes.get(j).getSender()+"'s Nwitte : ");
        adder();
    }
    public TwitteController(int counter,int i,String reter,int j) throws IOException {
        difiner();
        setter(counter,i);
        Sender.setText(" "+reter + " Renwitted from "+ twittes.get(i).getSender() +
                " | is comment of "+twittes.get(j).getSender()+" : ");
        adder();
    }
}
