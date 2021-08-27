package submit;

import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import lists.likeretList;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitteControl.TwitteController;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class submitRetwitte {
    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    jsonUsers get_j = new jsonUsers();
    List<objUsers> users = get_j.get();

    public submitRetwitte(){}
    private static final Logger logger = LogManager.getLogger(submitRetwitte.class);
    public submitRetwitte(String reter,int [] mapper,int response,int counter,int type) {
        logger.info("System: user went to submit.submitRetwitte");
        response=response-1;
        int rets=0;
        if (type==1) {
        rets = Integer.parseInt(TwitteController.retwitteList.get(response).getText());
        }else if(type==2){
         rets = Integer.parseInt(TwitteController.cRetwitteList.get(response).getText());
        }
                if(twittes.get(mapper[response]).getRetwittes().contains(reter)){
                    twittes.get(mapper[response]).getRetwittes().remove(reter);
                    if (type==1) {
                        TwitteController.retwitteAction.get(response).getStyleClass().remove("retwitted");
                        TwitteController.retwitteAction.get(response).setText("Retwitte");
                        TwitteController.retwitteList.get(response).setText(String.valueOf(rets - 1));
                    }else if(type==2){
                        TwitteController.cRetwitteAction.get(response).getStyleClass().remove("retwitted");
                        TwitteController.cRetwitteAction.get(response).setText("Retwitte");
                        TwitteController.cRetwitteList.get(response).setText(String.valueOf(rets - 1));
                    }
                    logger.info("System: Nwitte Unretwitted");
                }else {
                    twittes.get(mapper[response]).getRetwittes().add(reter);
                    logger.info("System: Nwitte Retwitted");
                    if (type==1) {
                        TwitteController.retwitteAction.get(response).getStyleClass().add("retwitted");
                        TwitteController.retwitteAction.get(response).setText("Unretwitte");
                        TwitteController.retwitteList.get(response).setText(String.valueOf(rets + 1));
                    }else if(type==2){
                        TwitteController.cRetwitteAction.get(response).getStyleClass().add("retwitted");
                        TwitteController.cRetwitteAction.get(response).setText("Unretwitte");
                        TwitteController.cRetwitteList.get(response).setText(String.valueOf(rets + 1));
                    }
                }
                new jsonTwittes(twittes);
            }

    public void list(int [] mapper,int response,int counter,String username) throws IOException {

        int c=1;


        for (int i = twittes.get(mapper[response - 1]).getRetwittes().size() - 1; i > 0; i--)
            for (objUsers user : users)
                if (user.getUsername().equals(twittes.get(mapper[response - 1]).getRetwittes().get(i))
                        && !user.getBlocks().contains(username) && user.getIsEnable()) {
                    c++;
                }
                                if (c == 1)
                                    JOptionPane.showMessageDialog(null,"No body Renwitted this Nwitte!");
                                else {
                                    new likeretList(username, mapper[response - 1], 2);
                                }

    }
}
