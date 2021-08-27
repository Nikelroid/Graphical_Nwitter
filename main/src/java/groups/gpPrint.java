package groups;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import objects.objTwitte;
import objects.objUsers;
import userControl.userFinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class gpPrint {
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();

    userFinder uf = new userFinder();
    int target =0;

    AnchorPane Group;
    Label Name, Count;
    public static ArrayList<Button> Members, Messages, Delete;

    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    jsonUsers us = new jsonUsers();

    private void difiner() throws IOException {

        Group = FXMLLoader.load(getClass().getResource("/layout/cards/category_card.fxml"));

        Name = (Label) Group.lookup("#name");
        Count = (Label) Group.lookup("#count");
        Members.add((Button) Group.lookup("#member"));
        Messages.add((Button) Group.lookup("#message"));
        Delete.add((Button) Group.lookup("#delete"));

    }

    private void setter(int i) {
        Name.setText(users.get(target).getGroups().get(i).get(1));
        Count.setText("Serial: "+users.get(target).getGroups().get(i).get(0));
    }
    private void adder(){
        ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
        VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
        twitteList.getChildren().add(Group);
    }


    public gpPrint(String username) throws IOException {
        target = uf.UserFinder(username);
        for (int i = 0; i < users.get(target).getGroups().size(); i++) {
            difiner();
            setter(i);
            adder();
        }
    }
}
