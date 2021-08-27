package lists;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonUsers;
import objects.objUsers;
import userControl.userFinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class listPrinter {

    AnchorPane card;
    FXMLLoader fXMLLoader = new FXMLLoader();
    Label Username ,Name;

    public static ArrayList<Button> followButton;


    jsonUsers us = new jsonUsers();
    List<objUsers> users = us.get();

    public listPrinter() {
    }

    protected void difiner() throws IOException {
        card = FXMLLoader.load(getClass().getResource("/layout/cards/user_card.fxml"));
        followButton.add((Button) card.lookup("#follow"));
        Username= (Label) card.lookup("#username");
        Name= (Label) card.lookup("#name");
    }

    private void setter(int counter,int target) {

       Username.setText(users.get(target).getUsername());
       Name.setText(users.get(target).getName());
    }
    private void adder(){
        ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
        VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
        twitteList.getChildren().add(card);
    }



    public listPrinter(int counter,String username) throws IOException {
        userFinder uf = new userFinder();
        difiner();
        setter(counter,uf.UserFinder(username));
        adder();
    }
}
