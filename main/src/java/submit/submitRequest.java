package submit;

import jsonContoller.jsonNotifs;
import jsonContoller.jsonUsers;
import objects.objNotifs;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class submitRequest {
    jsonNotifs Not = new jsonNotifs();
    List<objNotifs> notifs = Not.get();
    jsonUsers get_j = new jsonUsers();
    List<objUsers> users = get_j.get();
    submitAction submit = new submitAction();
    File f = new File("Notifs.json");

    public submitRequest() {
    }

    private static final Logger logger = LogManager.getLogger(submitRequest.class);

    public void submitReq(String reqer, String requed) {
        logger.info("System: user went to submit.submitRequest");

        if (f.exists()) {
            notifs.add(new objNotifs(8, reqer, requed));
        } else {
            notifs = Collections.singletonList(
                    new objNotifs(8, reqer, requed));
        }
        new jsonNotifs(notifs);
    }

    public void submitUnreq(String unreqer, String unreqed) {


        if (f.exists()) {
            for (int i = 0; i < notifs.size(); i++) {
                if (notifs.get(i).getUser1().equals(unreqer) &&
                        notifs.get(i).getUser2().equals(unreqed) &&
                        notifs.get(i).getType() == 8) {
                    notifs.remove(i);
                    break;
                }
            }
        }
        new jsonNotifs(notifs);
    }

    public void submitReject(int numnotif, int[] mapper, int counter) {



                    logger.info("System: The request rejected");
        JOptionPane.showMessageDialog(null,"The request rejected");

        notifs.add(new objNotifs(10,
                notifs.get(mapper[numnotif]).getUser1(),
                notifs.get(mapper[numnotif]).getUser2()));
        notifs.remove(mapper[numnotif]);
        new jsonNotifs(notifs);

    }

    public void submitMuteAccept(int numnotif, int[] mapper, int counter) {
        String user1 = notifs.get(mapper[numnotif]).getUser1();
        String user2 = notifs.get(mapper[numnotif]).getUser2();
        submit.Submit_follow(user1,user2);
        accept(numnotif, mapper,user1,user2);
    }

    private void accept(int numnotif, int[] mapper,String user1, String user2) {
        notifs.remove(mapper[numnotif]);
        logger.info("System: The request Accepted!");
        JOptionPane.showMessageDialog(null,"Request Accepted");
        var uf = new userFinder();


            if (!users.get(uf.UserFinder(user2)).getFollowings().contains(user1))
                followBack(mapper, numnotif,user1,user2);

        new jsonNotifs(notifs);
    }

    public void submitAccept(int numnotif, int[] mapper, int counter) {
            String user1 = notifs.get(mapper[numnotif]).getUser1();
        String user2 = notifs.get(mapper[numnotif]).getUser2();
        submit.Submit_follow(user1, user2);
        notifs.add(new objNotifs(9,user1,user2));
        accept(numnotif, mapper,user1,user2);
    }

    private void followBack(int[] mapper, int numnotif,String user1,String user2) {
        int input = JOptionPane.showConfirmDialog(null,"Follow "
                +  user1 +" back?");
        if (input==0) {
            submit.Submit_follow(user2,user1);
            new jsonNotifs(notifs);
            logger.info("System: User followed back!");
            JOptionPane.showMessageDialog(null, user1
                    +"User followed back!");
        }

        new jsonNotifs(notifs);
    }

}
