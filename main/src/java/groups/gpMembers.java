package groups;

import imageControl.getImageFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonUsers;
import objects.objUsers;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import userControl.userFinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class gpMembers {

    AnchorPane card;
    ImageView Icon;
    FXMLLoader fXMLLoader = new FXMLLoader();
    Label Username ,Name;
    ArrayList<Button> actionButton;
    jsonUsers get_j = new jsonUsers();
    int[] mapper = new int[10000];
    int[] mapperDeleteButton = new int[10000];
    int mapCount = 0;
    List<objUsers> users = get_j.get();
    userFinder us = new userFinder();
    int target=0;

    private static final Logger logger = LogManager.getLogger(gpMembers.class);
    private void definer() throws IOException {
        card = FXMLLoader.load(getClass().getResource("/layout/cards/category_members_card.fxml"));
        Username = (Label) card.lookup("#username");
        Name = (Label) card.lookup("#name");
        Icon = (ImageView) card.lookup("#icon");

    }

    private void setter (String username,int userNum){
        try {
            getImageFile getProfileFile = new getImageFile();
            File file = getProfileFile.profile(users.get(userNum).getUsername());
            Image image = new Image(file.toURI().toString());
            FileUtils.readFileToByteArray(file);
            Icon.setImage(image);
        } catch (NullPointerException| FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        actionButton.add((Button) card.lookup("#delete"));
        Username.setText(users.get(userNum).getUsername());
        Name.setText(users.get(userNum).getName());
    }
    private void adder () {
        ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
        VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
        twitteList.getChildren().add(card);
    }

    public gpMembers(String username,int gpNum,int type) throws IOException {
        target = us.UserFinder(username);

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.show();
        Button Back = (Button) launch.view.scene.lookup("#back");
        Back.setText("Back to Groups");

        Label Header = (Label) launch.view.scene.lookup("#header");
        Header.setText(users.get(target).getGroups().get(gpNum).get(1) + " Groups");
        actionButton = new ArrayList<>();

        definer();
        users = get_j.get();
        if (type == 1) {

            actionButton.add((Button) card.lookup("#delete"));
            actionButton.get(0).setText("Add");
            actionButton.get(0).getStyleClass().remove("toggle_wrong");
            actionButton.get(0).getStyleClass().add("toggle_accept");
            Icon.setImage(new Image(getClass().getResourceAsStream("/img/plus.png")));
            Username.setText("For add member to this Group press the button");
            Name.setText("");
            adder();

            Back.setOnAction(event -> {
                try {
                    new gpMain(username);
                } catch (Exception e) {
                    logger.error("Error in back to Groups page");
                }
            });
            actionButton.get(0).setOnAction(event -> {
                try {
                    new gpMembers(username, gpNum, 2);
                } catch (IOException e) {
                    logger.error("Error in load add member type of Group");
                }
            });


            for (int i = 0; i < users.size(); i++) {
                for (int j = 0; j < users.get(i).getGroups().size(); j++)
                    if (users.get(i).getGroups().get(j).get(0)
                            .equals(users.get(target).getGroups().get(gpNum).get(0))
                            && users.get(i).getIsEnable()) {
                        definer();
                        setter(username, us.UserFinder(users.get(i).getUsername()));
                        adder();
                        mapperDeleteButton[mapCount] = i;
                        mapCount++;
                    }
            }
            for (int i = 0; i < mapCount; i++) {
                final int finalI = i;
                if (mapperDeleteButton[finalI] == target) actionButton.get(finalI + 1).setText("Leave");
                actionButton.get(i + 1).setOnAction(event -> {
                    for (int j = 0; j < users.get(mapperDeleteButton[finalI]).getGroups().size(); j++) {
                        if (users.get(mapperDeleteButton[finalI]).getGroups().get(j).get(0)
                                .equals(users.get(target).getGroups().get(gpNum).get(0))) {
                            users.get(mapperDeleteButton[finalI]).getGroups().remove(j);
                            break;
                        }
                    }

                    new jsonUsers(users);
                    try {
                        if (mapperDeleteButton[finalI] == target) new gpMain(username);
                        else new gpMembers(username, gpNum, 1);
                    } catch (IOException e) {
                        logger.error("Error in reload catMembers after delete a member");
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("Error in Left a group catMembers after delete a member");
                    }
                });
            }
        } else {
            Back.setOnAction(event -> {
                try {
                    new gpMembers(username, gpNum, 1);
                } catch (Exception e) {
                    logger.error("Error in back to category members page");
                }
            });
            int counter = 0;
            int[] mapper = new int[10000];

            for (int i = 1; i < users.get(target).getFollowings().size(); i++) {
                boolean exist = true;
                int fuser = us.UserFinder(users.get(target).getFollowings().get(i));
                for (int j = 0; j < users.get(fuser).getGroups().size(); j++)
                    if (users.get(fuser).getGroups().get(j).get(0).equals
                            (users.get(target).getGroups().get(gpNum).get(0))
                            || !users.get(us.UserFinder(
                            users.get(target).getFollowings().get(i))).getIsEnable()) {
                        exist = false;
                        break;
                    }

                if (exist) {
                    definer();
                    setter(username, us.UserFinder(users.get(target).getFollowings().get(i)));
                    adder();
                    mapper[counter] = us.UserFinder(users.get(target).getFollowings().get(i));
                    counter++;
                }
            }
        for (int i = 0; i < actionButton.size(); i++) {
            actionButton.get(i).setText("Add");
            actionButton.get(i).getStyleClass().remove("toggle_wrong");
            actionButton.get(i).getStyleClass().add("toggle_accept");
        }
        for (int i = 0; i < actionButton.size(); i++) {
            final int finalI = i;
            actionButton.get(i).setOnAction(event -> {
                ArrayList<String> newGp = new ArrayList<>();
                newGp.add(users.get(target).getGroups().get(gpNum).get(0));
                newGp.add(users.get(target).getGroups().get(gpNum).get(1));
                users.get(mapper[finalI]).getGroups().add(newGp);
                new jsonUsers(users);
                try {
                    new gpMembers(username, gpNum, 2);
                } catch (IOException e) {
                    logger.error("Error in reload catMembers after delete a member");
                }
            });
        }
    }
        }
    }

