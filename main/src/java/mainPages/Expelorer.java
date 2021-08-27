package mainPages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitMessage;
import twitteControl.Menu;
import twitteControl.TwitteController;
import twitteControl.twittePrinter;
import userControl.userFinder;
import userControl.userProfile;

import javax.swing.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Expelorer {
    Menu menu = new Menu();
    AnchorPane SearchBar;
    String username;
    int counter = 1;
    int[] mapper = new int[10000];
    jsonUsers Users_get = new jsonUsers();
    List<objUsers> users = Users_get.get();
    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    Feed feed = new Feed();
    ArrayList<Integer> retorder = new ArrayList<Integer>();
    int target = 0;
    int tar = 0;
    submitMessage sb = new submitMessage();
    int[] cheat = new int[10000];
    int sn = 1;
    private static final Logger logger = LogManager.getLogger(Expelorer.class);

    public Expelorer(String username) throws Exception {
        logger.info("System: user went to mainPages.Expelorer");
        this.username = username;
        File f = new File("Twittes.json");
        if (f.exists()) {

            launch.view.root = null;
            launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/general_page.fxml"));

            launch.view.scene = new Scene(launch.view.root);
            launch.view.stage.setScene(launch.view.scene);
            launch.view.stage.setTitle("Explorer");
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
            Header.setText("EXPLORER");
            TwitteController.likeList = new ArrayList<>();
            TwitteController.likeAction = new ArrayList<>();
            TwitteController.commentList = new ArrayList<>();
            TwitteController.commentAction = new ArrayList<>();
            TwitteController.retwitteList = new ArrayList<>();
            TwitteController.retwitteAction = new ArrayList<>();
            TwitteController.Share = new ArrayList<>();
            TwitteController.Save = new ArrayList<>();
            TwitteController.Report = new ArrayList<>();
            TwitteController.twitteImages=new ArrayList<>();
            TwitteController.twitteImages=new ArrayList<>();

            SearchBar = FXMLLoader.load(getClass().getResource("/layout/header/search_bar.fxml"));
            ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
            VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
            twitteList.getChildren().add(SearchBar);

            for (int j = twittes.size() - 1; j >= 0; j--)
                for (int i = 0; i < users.size(); i++)
                    if (twittes.get(j).getRetwittes().contains(users.get(i).getUsername()))
                        for (int k = 0; k < users.size(); k++)
                            if (users.get(k).getUsername().equals(twittes.get(j).getSender()))
                                if (!(users.get(k).getBlocks().contains(username)) &&

                                        !(users.get(i).getBlocks().contains(username)) &&
                                        !(users.get(target).getMutes().contains(users.get(i).getUsername()))
                                        && !(users.get(target).getBlocks().contains(users.get(i).getUsername()))
                                        && !(retorder.contains(twittes.get(j).getSerial()))
                                        && (users.get(k).getIsEnable())   && feed.repChecker(j) &&
                                        (users.get(k).getAccount() || users.get(k).getFollowers().contains(username))
                                        || users.get(k).getUsername().equals(username)) {
                                    boolean com = true;
                                    for (int l = 0; l < twittes.size(); l++)
                                        if (twittes.get(l).getComments().contains(twittes.get(j).getSerial())) {
                                            new twittePrinter(counter, j, users.get(i).getUsername(), l);
                                            com = false;
                                            break;
                                        }
                                    if (com) new twittePrinter(counter, j, users.get(i).getUsername());


                                    retorder.add(twittes.get(j).getSerial());
                                    mapper[counter - 1] = j;
                                    counter++;
                                }

            for (int i = twittes.size() - 1; i >= 0; i--)
                for (int j = 0; j < users.size(); j++)
                    if (users.get(j).getUsername().equals(username))
                        for (int k = 0; k < users.size(); k++)
                            if (users.get(k).getUsername().equals(twittes.get(i).getSender()))
                                if (!(users.get(k).getBlocks().contains(username)) &&
                                        !(users.get(j).getMutes().contains(twittes.get(i).getSender()))
                                        && !(users.get(j).getBlocks().contains(twittes.get(i).getSender()))
                                        && !(retorder.contains(twittes.get(i).getSerial()))
                                        && (users.get(k).getIsEnable()) && feed.repChecker(i) &&
                                        (users.get(k).getAccount() || users.get(k).getFollowers().contains(username))
                                        || users.get(k).getUsername().equals(username)) {
                                    boolean com = true;
                                    for (int l = 0; l < twittes.size(); l++)
                                        if (twittes.get(l).getComments().contains(twittes.get(i).getSerial())) {
                                            new twittePrinter(counter, i, l);
                                            com = false;
                                            break;
                                        }
                                    if (com) new twittePrinter(counter, i);

                                    mapper[counter - 1] = i;
                                    counter++;
                                }
        }
        if (counter == 1) new twittePrinter();
        menu.Menu_command(username);
        menu.twitteButtons(username,mapper,counter);
        searching(username);
        launch.view.scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    searching(username);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Button searchButton = (Button) SearchBar.lookup("#search_button");
        searchButton.setOnAction(event -> {

            try {
                searching(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void searching(String username) throws Exception {

        TextField searchField = (TextField) SearchBar.lookup("#search_field");


            String response = searchField.getText();
            if (!response.isEmpty()) {
                for (int i = 0; i < users.size(); i++)
                    if (users.get(i).getUsername().toLowerCase(Locale.ROOT).
                            equals(response.toLowerCase(Locale.ROOT))) {
                        for (int j = 0; j < users.size(); j++)
                            if (!(users.get(i).getBlocks().contains(username)) && users.get(i).getIsEnable()) {
                                try {
                                    new userProfile(username, users.get(i).getUsername());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                    } else if (users.get(i).getName().toLowerCase(Locale.ROOT).equals(response.toLowerCase(Locale.ROOT))) {
                        if (!(users.get(i).getBlocks().contains(username)) && users.get(i).getIsEnable()) {
                            try {
                                new userProfile(username, users.get(i).getUsername());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                JOptionPane.showMessageDialog(null, "Username or name not found");
            }
    }
}

