package userControl;

import chat.chatPrinter;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import objects.objUsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    public class usersPrint {

        public static ArrayList<AnchorPane> user;
        jsonUsers get_users = new jsonUsers();
        List<objUsers> users = get_users.get();

        userFinder uf = new userFinder();
        int target =0;
        ImageView Icon;
        Label Name, Username,nonSeen;
        public static ArrayList<Button> Delete;

        jsonTwittes Get = new jsonTwittes();

        private void difiner(int counter) throws IOException {

            user.add(FXMLLoader.load(getClass().getResource("/layout/cards/user_messanger_card.fxml")));
            Username = (Label) user.get(counter-1).lookup("#username");
            Name = (Label) user.get(counter-1).lookup("#name");
            nonSeen = (Label) user.get(counter-1).lookup("#nonseen");
            Delete.add((Button) user.get(counter-1).lookup("#delete"));
            Icon= (ImageView) user.get(counter-1).lookup("#icon");
        }

        private void setter(int i,int nonseen) {
            try {
                Icon.setImage(new Image(chatPrinter.class.getResourceAsStream("/profiles/"+
                        users.get(target).getUsername()+".png")));
            }catch (NullPointerException ignored){
            }
            Username.setText(users.get(target).getUsername());
            Name.setText(users.get(target).getName());
            nonSeen.setText(" "+nonseen+" ");
        }
        private void adder(int counter){
            ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
            VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
            twitteList.getChildren().add(user.get(counter-1));
        }


        public usersPrint(String username,int nsm,int counter) throws IOException {
            target = uf.UserFinder(username);
                difiner(counter);
                setter(target,nsm);
                adder(counter);
        }

    }

