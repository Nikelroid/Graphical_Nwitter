package mainPages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import jsonContoller.jsonReports;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import objects.objReport;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitteControl.Menu;
import twitteControl.TwitteController;
import twitteControl.twittePrinter;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Feed {


    public Feed() {
    }
    Menu menu = new Menu();
    int counter = 1;
    public static String username;
    int[] mapper = new int[10000];
    ArrayList<Integer> retorder = new ArrayList<Integer>();
    jsonTwittes Get = new jsonTwittes();
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    List<objTwitte> twittes = Get.get();
    private static final Logger logger = LogManager.getLogger(Feed.class);

    public Feed(String username) throws Exception {
        Feed.username =username;
        logger.info("System: user went to mainPages.Feed");
        int search = 0;
        File f = new File("Twittes.json");
        if (f.exists()) {
            int target = 0;
            userFinder userFinder = new userFinder();
            target = userFinder.UserFinder(username);


            launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/general_page.fxml"));

            launch.view.scene = new Scene(launch.view.root);
            launch.view.stage.setScene(launch.view.scene);
            launch.view.stage.setTitle("Feed");
            launch.view.stage.show();
            Label Header = (Label) launch.view.scene.lookup("#header");
            Header.setText("Feed");

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

            TwitteController.likeList=new ArrayList<>();
            TwitteController.likeAction=new ArrayList<>();
            TwitteController.commentList=new ArrayList<>();
            TwitteController.commentAction=new ArrayList<>();
            TwitteController.retwitteList=new ArrayList<>();
            TwitteController.retwitteAction=new ArrayList<>();
            TwitteController.Share=new ArrayList<>();
            TwitteController.Save=new ArrayList<>();
            TwitteController.Report=new ArrayList<>();
            TwitteController.twitteImages=new ArrayList<>();

            for (int i = 0; i < users.get(target).getFollowings().size(); i++)
                for (int j = twittes.size() - 1; j >= 0; j--)
                    if (twittes.get(j).getRetwittes().contains(username) &&
                            !(retorder.contains(twittes.get(j).getSerial()))
                            && users.get(target).getIsEnable() && repChecker(j)) {
                        boolean com = true;
                        for (int l = 0; l < twittes.size(); l++)
                            if (twittes.get(l).getComments().contains(twittes.get(j).getSerial())) {
                                new twittePrinter(counter, j, "You", l);
                                com = false;
                                break;
                            }
                        if (com) new twittePrinter(counter, j, "You");

                        retorder.add(twittes.get(j).getSerial());
                        mapper[counter - 1] = j;
                        counter++;
                    } else if (twittes.get(j).getRetwittes().contains(users.get(target).getFollowings().get(i)))
                        for (int k = 0; k < users.size(); k++)
                            if (users.get(k).getUsername().equals(twittes.get(j).getSender()) &&
                                    (users.get(k).getIsEnable()))
                                if (!(users.get(k).getBlocks().contains(username)) &&
                                        !(retorder.contains(twittes.get(j).getSerial())) &&
                                        !(users.get(target).getMutes().contains(users.get(target).getFollowings().get(i)))
                                        && repChecker(j)) {
                                    boolean com = true;
                                    for (int l = 0; l < twittes.size(); l++)
                                        if (twittes.get(l).getComments().contains(twittes.get(j).getSerial())) {
                                            new twittePrinter(counter, j, users.get(target).getFollowings().get(i), l);
                                            com = false;
                                            break;
                                        }
                                    if (com) new twittePrinter(counter, j, users.get(target).getFollowings().get(i));

                                    retorder.add(twittes.get(j).getSerial());
                                    mapper[counter - 1] = j;
                                    counter++;
                                }


            for (int i = twittes.size() - 1; i >= 0; i--)
                if ((users.get(target).getFollowings().contains(twittes.get(i).getSender())
                        || (twittes.get(i).getSender().equals(username))))
                    for (int j = 0; j < users.size(); j++)
                        if (users.get(j).getUsername().equals(username))
                            for (int k = 0; k < users.size(); k++)
                                if (users.get(k).getUsername().equals(twittes.get(i).getSender()) &&
                                        (users.get(k).getIsEnable()))
                                    if (!(users.get(k).getBlocks().contains(username)) &&
                                            !(users.get(j).getMutes().contains(twittes.get(i).getSender()))
                                            && !(users.get(j).getBlocks().contains(twittes.get(i).getSender()))
                                            && !(retorder.contains(twittes.get(i).getSerial()))
                                            && repChecker(i)) {

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
    }
    jsonReports getReps = new jsonReports();
    List<objReport> reports = getReps.get();
    public boolean repChecker(int i){
        reports = getReps.get();
        twittes = Get.get();
        int reps=0;
        for (objReport report : reports) {
            if (report.getSerial() == twittes.get(i).getSerial())
                reps++;
        }
        return (reps<6);
    }

}
