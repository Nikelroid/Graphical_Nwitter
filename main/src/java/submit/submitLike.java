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

public class submitLike {
    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    jsonUsers get_j = new jsonUsers();
    List<objUsers> users = get_j.get();

    public submitLike(){}
    private static final Logger logger = LogManager.getLogger(submitLike.class);
    public submitLike(String liker,int [] mapper,int response,int counter,int type) {
        logger.info("System: user went to submit.submitLike");
        response = response-1;
        int like=0;
        if (type==1) {
            like =  Integer.parseInt(TwitteController.likeList.get(response).getText());
        }else if(type==2){
            like =  Integer.parseInt(TwitteController.cLikeList.get(response).getText());
        }
            if (twittes.get(mapper[response]).getLikes().contains(liker)) {
                twittes.get(mapper[response]).getLikes().remove(liker);
                if (type==1) {
                    TwitteController.likeAction.get(response).getStyleClass().remove("liked");
                    TwitteController.likeAction.get(response).setText("Like");
                    TwitteController.likeList.get(response).setText(String.valueOf(like - 1));
                }else if (type==2){
                    TwitteController.cLikeAction.get(response).getStyleClass().remove("liked");
                    TwitteController.cLikeAction.get(response).setText("Like");
                    TwitteController.cLikeList.get(response).setText(String.valueOf(like - 1));
                }
                logger.info("System: unLiked!");
            } else {
                twittes.get(mapper[response]).getLikes().add(liker);
                if (type==1) {
                    TwitteController.likeAction.get(response).getStyleClass().add("liked");
                    TwitteController.likeAction.get(response).setText("Unlike");
                    TwitteController.likeList.get(response).setText(String.valueOf(like + 1));
                }else if (type==2){
                    TwitteController.cLikeAction.get(response).getStyleClass().add("liked");
                    TwitteController.cLikeAction.get(response).setText("Unlike");
                    TwitteController.cLikeList.get(response).setText(String.valueOf(like + 1));
                }
                logger.info("System: Liked!");
            }
            new jsonTwittes(twittes);




    }
    public void list(int [] mapper,int response,int counter,String username) throws IOException {
        logger.info("System: Like list opened");
        int c=1;


                if (twittes.get(mapper[response - 1]).getLikes().size() != 1) {
                    StringBuilder text = new StringBuilder("Like:");
                    for (int i = twittes.get(mapper[response - 1]).getLikes().size() - 1; i > 0; i--)
                        for (int j = 0; j < users.size(); j++)
                            if (users.get(j).getUsername().equals(twittes.get(mapper[response - 1]).getLikes().get(i))
                                    && !users.get(j).getBlocks().contains(username) && users.get(j).getIsEnable()) {
                                c++;
                            }
                    if (c==1)
                        JOptionPane.showMessageDialog(null,"No body likes this Nwitte!");
                    else {
                        new likeretList(username,mapper[response - 1],1);
                    }

                }
            }
    }

