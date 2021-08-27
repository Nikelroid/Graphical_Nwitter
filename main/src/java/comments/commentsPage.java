package comments;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reports.Reports;
import submit.*;
import twitteControl.TwitteController;
import twitteControl.twittePrinter;
import userControl.userFinder;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class commentsPage {
    String username;
    List<Integer> serial;
    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    jsonUsers Getu = new jsonUsers();
    List<objUsers> users = Getu.get();
    int counter = 1;
    private static final Logger logger = LogManager.getLogger(commentsPage.class);

    int[] mapper = new int[10000];
    int target;
    submitMessage sb = new submitMessage();
    int[] cheat = new int [10000];
    int sn=1,entery;
    public commentsPage(List<Integer> serial, String username) throws Exception {
        logger.info("System: user went to comments.commentsPage");

        userFinder userFinder = new userFinder();
        target = userFinder.UserFinder(username);

        Button Back = (Button) submitComment.cScene.lookup("#back");
        Back.setText("Back");
        Label Header = (Label) submitComment.cScene.lookup("#header");
        Header.setText("comments page");

        Back.setOnAction(event -> {
            TwitteController.cLikeList=new ArrayList<>();
            TwitteController.cLikeAction=new ArrayList<>();
            TwitteController.cCommentList=new ArrayList<>();
            TwitteController.cCommentAction=new ArrayList<>();
            TwitteController.cRetwitteList=new ArrayList<>();
            TwitteController.cRetwitteAction=new ArrayList<>();
            TwitteController.cShare=new ArrayList<>();
            TwitteController.cSave=new ArrayList<>();
            TwitteController.cReport=new ArrayList<>();
            Back.getScene().getWindow().hide();
        });
        submitComment.cStage.show();
        this.serial=serial;
        this.username=username;

        TwitteController.cLikeList=new ArrayList<>();
        TwitteController.cLikeAction=new ArrayList<>();
        TwitteController.cCommentList=new ArrayList<>();
        TwitteController.cCommentAction=new ArrayList<>();
        TwitteController.cRetwitteList=new ArrayList<>();
        TwitteController.cRetwitteAction=new ArrayList<>();
        TwitteController.cShare=new ArrayList<>();
        TwitteController.cSave=new ArrayList<>();
        TwitteController.cReport=new ArrayList<>();

        TwitteController.twitteImages=new ArrayList<>();

        for (int i = twittes.size()-1; i >= 0; i--) {
            if (serial.contains(twittes.get(i).getSerial()))
                for (int j = 0; j < users.size(); j++) {
                    if (twittes.get(i).getSender().equals(users.get(j).getUsername()) &&
                            !(users.get(j).getBlocks().contains(username))
                    &&users.get(j).getIsEnable() &&  (users.get(j).getAccount()||users.get(j).getFollowers().contains(username)
                            || users.get(j).getUsername().equals(username))) {
                       new twittePrinter("big",counter,i);

                        mapper[counter - 1] = i;
                        counter++;
                    }
                }
        }


        for (int i = 1; i < counter; i++) {
            final int finalI = i;
            TwitteController.cLikeList.get(i - 1).setOnAction(event -> {
                try{
                    submitLike Like = new submitLike();
                    Like.list(mapper, finalI, counter, username);
                }catch(IndexOutOfBoundsException | IOException error){
                    JOptionPane.showMessageDialog(null,"This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, username);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            TwitteController.cLikeAction.get(i - 1).setOnAction(event -> {
                try{
                    new submitLike(username, mapper, finalI, counter, 2);
                }catch(IndexOutOfBoundsException error){
                    JOptionPane.showMessageDialog(null,"This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, username);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            TwitteController.cCommentList.get(i - 1).setOnAction(event -> {
                try{
                    submitComment Com = new submitComment();
                    try {
                        Com.list(mapper, finalI, counter, username);
                    } catch (IOException e) {
                        logger.error("Error: in loading comment list");
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }catch(IndexOutOfBoundsException error){
                    JOptionPane.showMessageDialog(null,"This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, username);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            TwitteController.cCommentAction.get(i - 1).setOnAction(event -> {
                try{
                    new submitComment(username, mapper, finalI, counter, 2);
                }catch(IndexOutOfBoundsException error){
                    JOptionPane.showMessageDialog(null,"This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, username);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            TwitteController.cRetwitteList.get(i - 1).setOnAction(event -> {
                try{
                    submitRetwitte Ret = new submitRetwitte();
                    Ret.list(mapper, finalI, counter, username);
                }catch(IndexOutOfBoundsException | IOException error){
                    JOptionPane.showMessageDialog(null,"This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, username);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            TwitteController.cRetwitteAction.get(i - 1).setOnAction(event -> {
                try{
                    new submitRetwitte(username, mapper, finalI, counter, 2);
                }catch(IndexOutOfBoundsException error){
                    JOptionPane.showMessageDialog(null,"This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, username);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            TwitteController.cShare.get(i - 1).setOnAction(event -> {
                try{
                    new submitShare(username, finalI, mapper);
                }catch(IndexOutOfBoundsException | IOException error){
                    JOptionPane.showMessageDialog(null,"This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, username);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            TwitteController.cSave.get(i - 1).setOnAction(event -> {
                try{
                    submitShare sS = new submitShare();
                    sS.submitSave(username, finalI, mapper, 2);
                }catch(IndexOutOfBoundsException error){
                    JOptionPane.showMessageDialog(null,"This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, username);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            TwitteController.cReport.get(i - 1).setOnAction(event -> {
                try{
                    String reptext = JOptionPane.showInputDialog("Insert your philosophy of report");
                    new Reports(username, reptext, mapper, finalI, counter);
                }catch(IndexOutOfBoundsException error){
                    JOptionPane.showMessageDialog(null,"This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, username);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
        }


        }



}
