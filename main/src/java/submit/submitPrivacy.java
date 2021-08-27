package submit;

import javafx.scene.control.Button;
import jsonContoller.jsonUsers;
import mainPages.Setting;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import userControl.userFinder;

import java.util.List;

public class submitPrivacy {
    jsonUsers get_j =new jsonUsers();
    List<objUsers> users= get_j.get();
    int target;
    private static final Logger logger = LogManager.getLogger(submitPrivacy.class);
    String username;
    Button toggleLastseen,toggleEmail,togglePhonenumber,toggleBirthday;

    public submitPrivacy(String username) throws Exception {
        this.username=username;

        toggleLastseen = (Button) launch.view.root.lookup("#toggle_lastseen");
        toggleEmail = (Button) launch.view.root.lookup("#toggle_email");
        togglePhonenumber = (Button) launch.view.root.lookup("#toggle_phonenumber");
        toggleBirthday = (Button) launch.view.root.lookup("#toggle_birthday");


        logger.info("System: user went to submit.submitPrivacy");

        var uf = new userFinder();
        target = uf.UserFinder(username);
        buttonStyleSetter();
        toggleLastseen.setOnAction(event -> {
            try {
                privacySwitcher(0);
            } catch (Exception e) {
                logger.error("Error in change privacy of Last seen");
            }
        });
        toggleBirthday.setOnAction(event -> {
            try {
                privacySwitcher(1);
            } catch (Exception e) {
                logger.error("Error in change privacy of Birthday date");
            }
        });
        toggleEmail.setOnAction(event -> {
            try {
                privacySwitcher(2);
            } catch (Exception e) {
                logger.error("Error in change privacy of Email");
            }
        });
        togglePhonenumber.setOnAction(event -> {
            try {
                privacySwitcher(3);
            } catch (Exception e) {
                logger.error("Error in change privacy of Phone number");
            }
        });

    }

    public void privacySwitcher(int i) throws Exception {
        int stt;
        if (users.get(target).getPrivacy().get(i)==3)
            stt = 1;
        else
            stt = users.get(target).getPrivacy().get(i) + 1;
            privacySetter(i,stt);
    }
    public void privacySetter(int i,int j) throws Exception {
        users.get(target).getPrivacy().set(i,j);
        new jsonUsers(users);
        new Setting(username);
        logger.info("System: Privacy status saved");
    }
    public void buttonStyleSetter(){
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0 -> style(toggleLastseen, users.get(target).getPrivacy().get(i));
                case 1 -> style(toggleBirthday, users.get(target).getPrivacy().get(i));
                case 2 -> style(toggleEmail, users.get(target).getPrivacy().get(i));
                case 3 -> style(togglePhonenumber, users.get(target).getPrivacy().get(i));
            }

        }
    }
    public void style(Button B,int type){
        switch (type) {
            case 1 -> {
                B.getStyleClass().remove("login");
                B.getStyleClass().remove("toggle_wrong");
                B.getStyleClass().add("toggle_accept");
                B.setText("Everyone");
            }
            case 2 -> {
                B.getStyleClass().remove("toggle_accept");
                B.getStyleClass().remove("toggle_wrong");
                B.getStyleClass().add("toggle_regular");
                B.setText("Your followings");
            }
            case 3 -> {
                B.getStyleClass().remove("login");
                B.getStyleClass().remove("toggle_accept");
                B.getStyleClass().add("toggle_wrong");
                B.setText("Nobody");
            }
        }

    }
}
