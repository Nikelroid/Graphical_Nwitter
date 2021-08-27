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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class oRequests {
    int counter = 0;
    int[] mapper = new int[10000];
    jsonNotifs Not = new jsonNotifs();
    List<objNotifs> notifs = Not.get();
    jsonUsers Getu = new jsonUsers();
    List<objUsers> users = Getu.get();
    Button Notifs,oReqs,uReqs,Back,Clear;
    submitRequest sr = new submitRequest();
    userFinder uf = new userFinder();
    File f = new File("Notifs.json");
    private static final Logger logger = LogManager.getLogger(Notifications.class);
    public oRequests(String username) throws Exception {

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
                logger.error("Error in go to o-reqs page ");
            }
        });
        Notifs.setOnAction(event -> {
            try {
                new Notifications(username,1);
            } catch (Exception e) {
                logger.error("Error in refreshing o-req page ");
            }
        });
        Back.setOnAction(event -> {
            try {
                new Info(username);
            } catch (Exception e) {
                logger.error("Error in Back to home from o-req page");
            }
        });
        Label Header = (Label) launch.view.scene.lookup("#header");
        Header.setText("Requests");

        oReqPrint.Reject=new ArrayList<>();
        oReqPrint.Accept1=new ArrayList<>();
        oReqPrint.Accept2=new ArrayList<>();

        for (int i = notifs.size() - 1; i >= 0; i--)
            if (notifs.get(i).getType() == 8
                    && notifs.get(i).getUser2().equals(username)&&
                    users.get(uf.UserFinder(notifs.get(i).getUser1())).getIsEnable()) {
                new oReqPrint(i);
                mapper[counter] = i;;
                counter++;
            }
        if(counter!=0) {
            for (int i = 0; i < oReqPrint.Accept1.size(); i++) {
                final int finalI = i;
                oReqPrint.Accept1.get(i).setOnAction(event -> {
                    sr.submitAccept(finalI, mapper,counter);
                    try {
                        new oRequests(username);
                    } catch (Exception e) {
                        logger.error("Error in refresh o-req page after accept a request");
                    }
                });
                oReqPrint.Accept2.get(i).setOnAction(event -> {
                    sr.submitMuteAccept(finalI, mapper,counter);
                    try {
                        new oRequests(username);
                    } catch (Exception e) {
                        logger.error("Error in refresh o-req page after mute accept a request");
                    }
                });
                oReqPrint.Reject.get(i).setOnAction(event -> {
                    sr.submitReject(finalI, mapper,counter);
                    try {
                        new oRequests(username);
                    } catch (Exception e) {
                        logger.error("Error in refresh o-req page after reject a request");
                    }
                });
            }
            Clear.setOnAction(event -> {
                for (int i = notifs.size() - 1; i >= 0; i--)
                    if (notifs.get(i).getUser2().equals(username) && notifs.get(i).getType()==8 ) {
                        notifs.remove(i);
                }
                new jsonNotifs(notifs);
                try {
                    new oRequests(username);
                } catch (Exception e) {
                    logger.error("Error in reloading notification page after clear notifs");
                }
            });
        }
    }


}
