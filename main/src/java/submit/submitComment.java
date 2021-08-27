package submit;

import comments.commentsPage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import objects.objTwitte;
import objects.objUsers;
import twitteControl.Newtwitte;
import twitteControl.TwitteController;
import userControl.userFinder;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class submitComment {
    jsonTwittes Get = new jsonTwittes();
        List<objTwitte> twittes = Get.get();

        Newtwitte com = new Newtwitte();
        int serial;
        public submitComment(){}
        public submitComment(String commenter,int [] mapper,int response,int counter,int type) {

                    var com = new Newtwitte();
                    serial = com.newComment(commenter);
                    if (serial!=-1) {
                        twittes = Get.get();
                        twittes.get(mapper[response - 1]).getComments().add(serial);
                        new jsonTwittes(twittes);
                        if (type==1) {
                            TwitteController.commentList.get(response - 1).setText(
                                    Integer.parseInt(
                                            TwitteController.commentList.get(response - 1).
                                                    getText()) + 1 + "");
                        }else if(type==2){
                            TwitteController.cCommentList.get(response - 1).setText(
                                    Integer.parseInt(
                                            TwitteController.cCommentList.get(response - 1).
                                                    getText()) + 1 + "");
                        }
                    }
        }

    public static Stage cStage;
    public static Parent cRoot;
    public static Scene cScene;



    public void list(int [] mapper,int response,int counter,String username) throws Exception {
            int c=1;



                    if (twittes.get(mapper[response - 1]).getComments().size()==1){
                        JOptionPane.showMessageDialog(null,"No body commented on this twitte");
                    }else {

                        cRoot = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));
                         cScene = new Scene(cRoot);
                         cStage = new Stage();
                        cStage.setScene(cScene);

                        jsonUsers getUser = new jsonUsers();
                        List<objUsers> users = getUser.get();
                        userFinder uf = new userFinder();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        ImageView Exit = (ImageView) launch.view.root.lookup("#exit") ;
                        Exit.setCursor(Cursor.HAND);
                        Exit.setOnMouseClicked(event -> {
                            int res = JOptionPane.showConfirmDialog(null,
                                    "Do you want to exit the app and stay online?");
                            if (res==0){
                                System.exit(1);
                            }else if (res==1) {
                                users.get(uf.UserFinder(username)).setLastseen(dtf.format(now));
                                new jsonUsers(users);
                                System.exit(1);
                            }
                        });

                        new commentsPage(twittes.get(mapper[response - 1]).getComments(),username);
                    }
    }
    }
