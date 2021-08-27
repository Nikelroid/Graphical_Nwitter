package lists;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import jsonContoller.jsonUsers;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import userControl.userFinder;
import userControl.userProfile;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Lists {
    int counter;
    String res;
    private static final Logger logger = LogManager.getLogger(Lists.class);
    public Lists(String username,String you,int response) throws Exception {

        listPrinter.followButton=new ArrayList<>();
        logger.info("System: user went to lists.Lists");
        counter=1;
        var get_j = new jsonUsers();
        int[] mapper = new int[10000];
        List<objUsers> users = get_j.get();
          userFinder us = new userFinder();
          int target = us.UserFinder(username);

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.show();

        userFinder uf = new userFinder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ImageView Exit = (ImageView) launch.view.root.lookup("#exit") ;
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int res = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app and stay online?");
            if (res==0){
                System.exit(1);
            }else if (res==1) {
                users.get(uf.UserFinder(username)).setLastseen(dtf.format(now));
                new jsonUsers(users);
                System.exit(1);
            }
        });

        Label Header = (Label) launch.view.scene.lookup("#header");
        Button Back = (Button) launch.view.scene.lookup("#back");
        var lp = new listPrinter();
        lp.difiner();
        Back.setText("Back to user page");
        Back.setOnAction(event -> {
            try {
                new userProfile(you,username);
            } catch (Exception e) {
                logger.error("Error in back to user page");
            }
        });

            switch (response) {
                case 1:
                    Header.setText(username + "'s Followings");
                    if (users.get(target).getFollowings().size() != 1){
                    for (int j = 1; j < users.get(target).getFollowings().size(); j++)
                        if (!(users.get(target).getBlocks().contains(you)))
                            for (int i = 0; i < users.size(); i++)
                                if (users.get(i).getUsername().equals(users.get(target).getFollowings().get(j))
                                        && users.get(i).getIsEnable()){
                                    new listPrinter(counter,users.get(target).getFollowings().get(j));
                        mapper[counter - 1] = j;
                        counter++;
                    }
            }

                        return;

                case 2:
                    if (users.get(target).getFollowers().size() != 1) {
                        Header.setText(username + "'s Followers");
                        for (int j = 1; j < users.get(target).getFollowers().size(); j++)
                            if (!(users.get(target).getBlocks().contains(you)))
                                for (int i = 0; i < users.size(); i++)
                                    if (users.get(i).getUsername().equals(users.get(target).getFollowers().get(j))
                                            && users.get(i).getIsEnable()){
                                        new listPrinter(counter,users.get(target).getFollowers().get(j));
                            mapper[counter - 1] = j;
                            counter++;
                        }
                    }


                        return;
                case 3:
                    if (users.get(target).getBlocks().size() != 1) {
                        Header.setText(username + "s Block list:");
                        for (int j = 1; j < users.get(target).getBlocks().size(); j++)
                            if (!(users.get(target).getBlocks().contains(you)))
                                for (int i = 0; i < users.size(); i++)
                                    if (users.get(i).getUsername().equals(users.get(target).getBlocks().get(j))
                                            && users.get(i).getIsEnable()){
                                        new listPrinter(counter,users.get(target).getBlocks().get(j));
                            mapper[counter - 1] = j;
                            counter++;
                        }
                    }

                return;

                    case 4:
                        if (users.get(target).getMutes().size()!=1) {
                            Header.setText(username + "s Mutes list:");
                            for (int j = 1; j < users.get(target).getMutes().size(); j++)
                                if (!(users.get(target).getBlocks().contains(you)))
                                    for (int i = 0; i < users.size(); i++)
                                        if (users.get(i).getUsername().equals(users.get(target).getMutes().get(j))
                                                && users.get(i).getIsEnable()){
                                            new listPrinter(counter,users.get(target).getMutes().get(j));
                                mapper[counter - 1] = j;
                                counter++;
                            }
                        }


            }
        }
    }

