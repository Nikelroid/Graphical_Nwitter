package userControl;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import jsonContoller.jsonUsers;
import objects.objUsers;

import java.util.List;

public class userController {
    userFinder uf = new userFinder();
    jsonUsers get_j = new jsonUsers();
    List<objUsers> users = get_j.get();
    public Label nameLabel,emailLabel,phonenumberLabel,birthdayLabel,bioLabel,
            lastSeen,Privacy,Status,followsYou,mutesYou,blocksYou;
    public Button followerCount,followingCount,blockCount,muteCount;
    public userController(String user,String username) {
        int target = uf.UserFinder(user);

        nameLabel = (Label) launch.view.root.lookup("#name_label");
        emailLabel = (Label) launch.view.root.lookup("#email_label");
        phonenumberLabel = (Label) launch.view.root.lookup("#phonenumber_label");
        birthdayLabel = (Label) launch.view.root.lookup("#birthday_label");
        bioLabel = (Label) launch.view.root.lookup("#bio_label");

        lastSeen = (Label) launch.view.root.lookup("#last_seen");
        Privacy = (Label) launch.view.root.lookup("#privacy");
        Status = (Label) launch.view.root.lookup("#status");

        followsYou = (Label) launch.view.root.lookup("#follows_you");
        mutesYou = (Label) launch.view.root.lookup("#mutes_you");
        blocksYou = (Label) launch.view.root.lookup("#blocks_you");

        followsYou.setText(" "+user+" follows you");
        mutesYou.setText(" "+user+" mutes you");
        blocksYou.setText(" "+user+" blocks you");

        followsYou.setVisible(false);
        mutesYou.setVisible(false);
        blocksYou.setVisible(false);

        followerCount = (Button) launch.view.root.lookup("#follower_count");
        followingCount = (Button) launch.view.root.lookup("#following_count");
        blockCount = (Button) launch.view.root.lookup("#block_count");
        muteCount = (Button) launch.view.root.lookup("#mute_count");

        if (!users.get(target).getLastseen().equals("Online")) {
            lastSeen.getStyleClass().remove("accept");
            lastSeen.getStyleClass().add("username");
        }

        nameLabel.setText(users.get(target).getName());



        emailLabel.setText("Email: "+users.get(target).getEmail());
        phonenumberLabel.setText("Phone number: "+users.get(target).getPhonenumber());
        birthdayLabel.setText("Birthday date: "+users.get(target).getBirthday());
        lastSeen.setText(" Last seen: "+users.get(target).getLastseen()+" ");

        if (users.get(target).getPrivacy().get(0)==3 ||
                (users.get(target).getPrivacy().get(0)==2 &&
                        !users.get(target).getFollowings().contains(username))) {
            lastSeen.setText(" Last seen: recently ");
            lastSeen.getStyleClass().remove("accept");
            lastSeen.getStyleClass().add("username");
        }
        if (user.equals(username)){
            lastSeen.setText(" Last seen: Online ");
            lastSeen.getStyleClass().add("accept");
            lastSeen.getStyleClass().remove("username");
        }
        if ( lastSeen.getText().equals(" Last seen: Online ")){
            lastSeen.getStyleClass().add("accept");
            lastSeen.getStyleClass().remove("username");
        }


        if (users.get(target).getPrivacy().get(1)==3 ||
                (users.get(target).getPrivacy().get(1)==2 &&
                        !users.get(target).getFollowings().contains(username)))
            birthdayLabel.setVisible(false);
        if (user.equals(username))  birthdayLabel.setVisible(true);
        if (users.get(target).getBirthday().equals("//")) birthdayLabel.setVisible(false);

        if (users.get(target).getPrivacy().get(2)==3 ||
                (users.get(target).getPrivacy().get(2)==2 &&
                        !users.get(target).getFollowings().contains(username)))
            emailLabel.setVisible(false);
        if (user.equals(username))  emailLabel.setVisible(true);
        if (users.get(target).getEmail().equals("")) emailLabel.setVisible(false);


        if (users.get(target).getPrivacy().get(3)==3 ||
                (users.get(target).getPrivacy().get(3)==2 &&
                        !users.get(target).getFollowings().contains(username)))
            phonenumberLabel.setVisible(false);
        if (user.equals(username))  phonenumberLabel.setVisible(true);
        if (users.get(target).getPhonenumber()==null) phonenumberLabel.setVisible(false);

        bioLabel.setText("Bio: "+users.get(target).getBio());


        if (users.get(target).getAccount()){
            Privacy.getStyleClass().remove("wrong");
            Privacy.setText(" Public ");
        }else{
            Privacy.getStyleClass().remove("accept");
            Privacy.getStyleClass().add("wrong");
            Privacy.setText(" Private ");
        }
        if (users.get(target).getIsEnable()) {
            Status.getStyleClass().remove("wrong");
            Status.setText(" Account is Enable ");
        }else{
            Status.getStyleClass().remove("accept");
            Status.getStyleClass().add("wrong");
            Status.setText(" Account is Disable ");
        }

    }
}
