package twitteControl;

import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class userTwittes {
    String username;
    int counter = 1, sounter = 1;
    int numtwitte;
    int[] mapper = new int[10000];
    jsonTwittes Get = new jsonTwittes();
    jsonUsers Users_get = new jsonUsers();
    List<objTwitte> twittes = Get.get();
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    submitMessage sb = new submitMessage();
    int[] cheat = new int[10000];
    int sn = 1;
    int target;
    private static final Logger logger = LogManager.getLogger(userTwittes.class);

    public userTwittes(String owner, String username) throws Exception {


        TwitteController.likeList = new ArrayList<>();
        TwitteController.likeAction = new ArrayList<>();
        TwitteController.commentList = new ArrayList<>();
        TwitteController.commentAction = new ArrayList<>();
        TwitteController.retwitteList = new ArrayList<>();
        TwitteController.retwitteAction = new ArrayList<>();
        TwitteController.Share = new ArrayList<>();
        TwitteController.Save = new ArrayList<>();
        TwitteController.Report = new ArrayList<>();
            TwitteController.twitteImages=new ArrayList<>();


        logger.info("System: user went to twitteControl.userTwittes");
        this.username = owner;
        File f = new File("Twittes.json");
        if (f.exists()) {

            for (int i = twittes.size() - 1; i >= 0; i--)
                if (twittes.get(i).getSender().equals(owner)) {
                    boolean ret = true;
                    for (int l = 0; l < twittes.size(); l++)
                        if (twittes.get(l).getComments().contains(twittes.get(i).getSerial())) {
                            new twittePrinter(counter, i, l);
                            ret = false;
                            break;
                        }
                    if (ret) new twittePrinter(counter, i);

                    mapper[counter - 1] = i;
                    counter++;
                }



            for (int i = twittes.size() - 1; i >= 0; i--)
                if (twittes.get(i).getRetwittes().contains(owner)) {
                    boolean ret = true;
                    for (int l = 0; l < twittes.size(); l++)
                        if (twittes.get(l).getComments().contains(twittes.get(i).getSerial())) {
                            new twittePrinter(counter, i, owner, l);
                            ret = false;
                            break;
                        }
                    if (ret) new twittePrinter(counter, i, owner);

                    mapper[counter - 1] = i;
                    counter++;
                }


            Menu menu = new Menu();
            menu.Menu_command(username);
            menu.twitteButtons(username, mapper, counter);

        }

    }
    public userTwittes() throws Exception {
        new twittePrinter(-1);
    }
}

