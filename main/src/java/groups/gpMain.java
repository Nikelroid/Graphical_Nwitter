package groups;

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
import jsonContoller.jsonUsers;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitteControl.Menu;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class gpMain {

    File id = new File("groupID.txt");
    int gpID;
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    int target;
    int counter=1,sounter = 1;
    public static String textMessage = null;
    private static final Logger logger = LogManager.getLogger(gpMain.class);

    AnchorPane creatCat;

    int[] mapper = new int [10000];
    int[] chater = new int [10000];

    public gpMain(String username) throws Exception {
        logger.info("System: user went to Groups");

        userFinder userFinder = new userFinder();
        target = userFinder.UserFinder(username);


        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/general_page.fxml"));

        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.setTitle("mainPages.Feed");
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
        Header.setText("Groups");


            creatCat = FXMLLoader.load(getClass().getResource("/layout/cards/category_main_card.fxml"));
            TextField catName = (TextField) creatCat.lookup("#name");
            catName.setPromptText("Insert the Group name");
            Button createCat = (Button) creatCat.lookup("#create");
            ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
            VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
            twitteList.getChildren().add(creatCat);


            createCat.setOnAction(event -> {
                if (!catName.getText().isEmpty()) {

                    try {
                        if (id.createNewFile()) {
                            gpID=100000;
                            FileWriter myWriter = new FileWriter("groupID.txt");
                            myWriter.write(gpID+"");
                            myWriter.close();

                        } else {

                            Scanner myReader = new Scanner(id);
                            String data = myReader.nextLine();
                            gpID = Integer.parseInt(data);
                            gpID++;
                            FileWriter myWriter = new FileWriter("groupID.txt");
                            myWriter.write(gpID+"");
                            myWriter.close();
                        }
                    } catch (IOException e) {
                        logger.error("Error in creating Group ID ");
                    }
                    ArrayList<String> newGp = new ArrayList<>();
                    newGp.add(gpID+"");
                    newGp.add(catName.getText());
                    users.get(target).getGroups().add(newGp);
                    new jsonUsers(users);
                    logger.info("System: Group created!");
                    try {
                        new gpMain(username);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("Error in reloading groups");
                    }
                }
            });
            launch.view.scene.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    if (!catName.getText().isEmpty()) {

                        try {
                            if (id.createNewFile()) {
                                gpID=100000;
                                FileWriter myWriter = new FileWriter("groupID.txt");
                                myWriter.write(gpID+"");
                                myWriter.close();

                            } else {

                                Scanner myReader = new Scanner(id);
                                String data = myReader.nextLine();
                                gpID = Integer.parseInt(data);
                                gpID++;
                                FileWriter myWriter = new FileWriter("groupID.txt");
                                myWriter.write(gpID+"");
                                myWriter.close();
                            }
                        } catch (IOException e) {
                            logger.error("Error in creating Group ID ");
                        }
                        ArrayList<String> newGp = new ArrayList<String>();
                        newGp.add(gpID+"");
                        newGp.add(catName.getText());
                        users.get(target).getGroups().add(newGp);
                        new jsonUsers(users);
                        logger.info("System: Group created!");
                        try {
                            new gpMain(username);
                        } catch (Exception e) {
                            logger.error("Error in reloading groups");
                        }
                    }
                }
            });


        gpPrint.Members = new ArrayList<>();
        gpPrint.Messages = new ArrayList<>();
        gpPrint.Delete = new ArrayList<>();
        new gpPrint(username);

        for (int i = 0; i < gpPrint.Delete.size(); i++) {
            final int finalI = i;
            gpPrint.Delete.get(i).setText("Leave");
            gpPrint.Delete.get(i).setOnAction(event -> {
                int input = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want leave this Group?");
                if (input==0) {
                    users.get(target).getGroups().remove(finalI);
                    new jsonUsers(users);
                    try {
                        new gpMain(username);
                    } catch (Exception e) {
                        logger.error("Error in reload groups");
                    }
                }
            });
            gpPrint.Messages.get(i).setOnAction(event -> {
                logger.info("User went to chat of "+finalI+1+"th group");
                try {
                    new gpChat(username,Integer.parseInt(users.get(target).getGroups().get(finalI).get(0)));
                } catch (IOException e) {
                    logger.error("Error in open groups members");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            gpPrint.Members.get(i).setOnAction(event -> {
                logger.info("User went to members of "+finalI+1+"th group");
                try {
                    new gpMembers(username, finalI, 1);
                } catch (IOException e) {
                    logger.error("Error in open groups members");
                }
            });
        }
        var menu = new Menu();
        menu.Menu_command(username);
    }

}
