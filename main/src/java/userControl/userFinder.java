package userControl;

import jsonContoller.jsonMessage;
import jsonContoller.jsonUsers;
import objects.objMessage;
import objects.objUsers;

import java.util.List;

public class userFinder {
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    jsonMessage getMessages = new jsonMessage();
    List<objMessage> messages = getMessages.get();
    public int UserFinder(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username))return i;
        }
        return 0;
    }
    public int messageFinder(int serial){
            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).getSerial()==serial)return i;
            }
            return 0;
        }
    }

