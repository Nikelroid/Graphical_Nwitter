package twitteControl;

import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class twittePrinter {
    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    jsonUsers us = new jsonUsers();
    List<objUsers> users = us.get();



    private static final Logger logger = LogManager.getLogger(twittePrinter.class);
    public twittePrinter() throws IOException {
        new TwitteController();
    }
    public twittePrinter(int k) throws IOException {
        if (k==-1)
        new TwitteController(-1);
    }
    public twittePrinter(int counter,int i) throws IOException {
        new TwitteController(counter,i);
    }
    public twittePrinter(String big,int counter,int i) throws IOException {
        new TwitteController(big,counter,i);
    }
    public twittePrinter(int counter,int i,String reter) throws IOException {
           new TwitteController(counter,i,reter);
    }
    public twittePrinter(int counter,int i,int j) throws IOException {
          new TwitteController(counter,i,j);
    }
    public twittePrinter(int counter,int i,String reter,int j) throws IOException {

        new TwitteController(counter,i,reter,j);
    }

}
