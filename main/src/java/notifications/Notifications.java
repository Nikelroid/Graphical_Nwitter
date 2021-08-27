package notifications;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import jsonContoller.jsonNotifs;
import jsonContoller.jsonUsers;
import mainPages.Info;
import objects.objNotifs;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitRequest;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Notifications {
    int counter = 0;
    int[] mapper = new int[10000];
    jsonNotifs Not = new jsonNotifs();
    List<objNotifs> notifs = Not.get();
    jsonUsers Getu = new jsonUsers();
    List<objUsers> users = Getu.get();
    Button Notifs,oReqs,uReqs,Back,Clear;
    submitRequest sr = new submitRequest();
    int[] tenet = new int[10000];
    File f = new File("Notifs.json");
    private static final Logger logger = LogManager.getLogger(Notifications.class);
    public Notifications(String username,int type) throws Exception {

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/notification_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.setTitle("Nwitter");
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

        Notifs = (Button) launch.view.scene.lookup("#notifs") ;
        uReqs = (Button) launch.view.scene.lookup("#u_reqs") ;
        oReqs = (Button) launch.view.scene.lookup("#o_reqs") ;
        Back = (Button) launch.view.scene.lookup("#back") ;
        Clear = (Button) launch.view.scene.lookup("#clear") ;

        Notifs.setOnAction(event -> {
            try {
                new Notifications(username,1);
            } catch (Exception e) {
                logger.error("Error in refreshing notification page ");
            }
        });
        uReqs.setOnAction(event -> {
            try {
                new Notifications(username,2);
            } catch (Exception e) {
                logger.error("Error in go to u-reqs notification page ");
            }
        });
        oReqs.setOnAction(event -> {
            try {
                new oRequests(username);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error in go to o-reqs page ");
            }
        });
        Back.setOnAction(event -> {
            try {
                new Info(username);
            } catch (Exception e) {
                logger.error("Error in Back to home from notifs page");
            }
        });

        Label Header = (Label) launch.view.scene.lookup("#header");
        if (type==1)
        Header.setText("notifications.Notifications");
        else
            Header.setText("Your requests");
        Notifs(username,type);
    }

    public void Notifs(String username,int type) throws Exception {
        notifsPrint.Dismiss = new ArrayList<>();
        notifs = Not.get();
        counter = 0;
        for (int i = notifs.size() - 1; i >= 0; i--)
            announcer(i, username,type);

        if (counter != 0){
            Clear.setOnAction(event -> {
                for (int i = notifs.size() - 1; i >= 0; i--) {
                    if (notifs.get(i).getUser2().equals(username) &&
                            notifs.get(i).getType() != 8 && notifs.get(i).getType() != 9
                            && notifs.get(i).getType() != 10
                    && type ==1) {
                        notifs.remove(i);
                    }else if(notifs.get(i).getUser1().equals(username)&&
                    (notifs.get(i).getType() == 9 || notifs.get(i).getType() == 10) && type == 2){
                        notifs.remove(i);
                    }
                }
                new jsonNotifs(notifs);
                try {
                    new Notifications(username,type);
                } catch (Exception e) {
                    logger.error("Error in reloading notification page after clear notifs");
                }
            });
        for (int i = 0; i < notifsPrint.Dismiss.size(); i++) {
            final int finalI = i;
            notifsPrint.Dismiss.get(i).setOnAction(event -> {
                notifs.remove(mapper[finalI]);
                new jsonNotifs(notifs);
                try {
                    new Notifications(username,type);
                } catch (Exception e) {
                    logger.error("Error in refresh notifs page after delete a notification");
                }
            });
        }
    }

    }
    userFinder uf = new userFinder();
    public void announcer(int i,String username,int type) throws IOException {
        if (notifs.get(i).getUser2().equals(username)&&
                users.get(uf.UserFinder(notifs.get(i).getUser1())).getIsEnable()
        && notifs.get(i).getType()!=8
                && notifs.get(i).getType()!=9
                && notifs.get(i).getType()!=10 && type==1) {
            new notifsPrint(i, notifs.get(i).getType(), counter,1);
            mapper[counter] = i;
            counter++;
        }else if (((notifs.get(i).getUser1().equals(username)&&
                users.get(uf.UserFinder(notifs.get(i).getUser2())).getIsEnable()
                && (notifs.get(i).getType()==9 ||notifs.get(i).getType()==10))
                || (notifs.get(i).getUser1().equals(username) && notifs.get(i).getType()==8 &&
                users.get(uf.UserFinder(notifs.get(i).getUser2())).getIsEnable()))&& type ==2){
            new notifsPrint(i, notifs.get(i).getType(), counter,2);
            mapper[counter] = i;
            counter++;
        }
    }

}
