package category;

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
import mainPages.Messenger;
import messengerChilds.massMessenger;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitMessage;
import twitteControl.Menu;
import userControl.userFinder;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Category {

    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();

    int target;
    public static String textMessage = null;
    private static final Logger logger = LogManager.getLogger(Category.class);

    AnchorPane creatCat;

    int[] mapper = new int [10000];
    int[] chater = new int [10000];

    public Category(String username,String textMessage) throws Exception {
        Category.textMessage=textMessage;
        new Category(username);
    }
    public Category(String username) throws Exception {
        logger.info("System: user went to category.Category");

        userFinder userFinder = new userFinder();
        target = userFinder.UserFinder(username);


        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/general_page.fxml"));

        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.setTitle("Category");
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
        Header.setText("Categories");

        if (Category.textMessage==null) {
            creatCat = FXMLLoader.load(getClass().getResource("/layout/cards/category_main_card.fxml"));
            TextField catName = (TextField) creatCat.lookup("#name");
            Button createCat = (Button) creatCat.lookup("#create");
            ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
            VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
            twitteList.getChildren().add(creatCat);


            createCat.setOnAction(event -> {
                if (!catName.getText().isEmpty()) {
                    users.get(target).getCategoiries().add(Collections.singletonList(catName.getText()));
                    new jsonUsers(users);
                    logger.info("System: category.Category created!");
                    try {
                        new Category(username);
                    } catch (Exception e) {
                        logger.error("Error in reloading categories");
                    }
                }
            });
            launch.view.scene.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    if (!catName.getText().isEmpty()) {
                        users.get(target).getCategoiries().add(Collections.singletonList(catName.getText()));
                        new jsonUsers(users);
                        logger.info("System: category.Category created!");
                        try {
                            new Category(username);
                        } catch (Exception e) {
                            logger.error("Error in reloading categories");
                        }
                    }
                }
            });
        }

        categoryPrint.Members = new ArrayList<>();
        categoryPrint.Messages = new ArrayList<>();
        categoryPrint.Delete = new ArrayList<>();
                new categoryPrint(username);

        for (int i = 0; i < users.get(target).getCategoiries().size()-1; i++) {
            if (Category.textMessage!=null) {
                categoryPrint.Delete.get(i).setVisible(false);
                categoryPrint.Members.get(i).setVisible(false);
                categoryPrint.Messages.get(i).setText("Select");
            }
            final int finalI = i;
            categoryPrint.Delete.get(i).setOnAction(event -> {
                int input = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want delete this category?");
                if (input==0) {
                    users.get(target).getCategoiries().remove(finalI + 1);
                    new jsonUsers(users);
                    try {
                        new Category(username);
                    } catch (Exception e) {
                        logger.error("Error in reload categories");
                    }
                }
            });
            categoryPrint.Messages.get(i).setOnAction(event -> {
              if (Category.textMessage==null){
                  try {
                      new massMessenger(username,finalI + 1);
                  } catch (Exception e) {
                      logger.error("Error in loading mass messenger from category");
                  }
              }else{
                  for (int j = 1; j < users.get(target).getCategoiries().get(finalI + 1).size(); j++) {
                      var sb = new submitMessage();
                      sb.SubMess(Category.textMessage, username, users.get(target).
                              getCategoiries().get(finalI + 1).get(j));
                  }
                  JOptionPane.showMessageDialog(null,"Message sent to members of "+
                          users.get(target).getCategoiries().get(finalI + 1).get(0) +" category");
                  try {
                      Category.textMessage= "";
                      massMessenger.Back.getScene().getWindow().hide();
                          new Messenger(username);
                      } catch (Exception e) {
                          logger.error(
                                  "error in reloading messenger after sending message from category class");
                      }

              }
            });
            categoryPrint.Members.get(i).setOnAction(event -> {
                logger.info("User went to members of "+finalI+1+"th category");
                    try {
                        new categoryMembers(username, finalI + 1, 1);
                    } catch (IOException e) {
                        logger.error("Error in open category members");
                    }
            });
        }
        var menu = new Menu();
        menu.Menu_command(username);
    }
}
