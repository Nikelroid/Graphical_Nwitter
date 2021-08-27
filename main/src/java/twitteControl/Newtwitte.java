package twitteControl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitTwitte;

import javax.swing.*;

public class Newtwitte {
    int serial;

    public Newtwitte() {

    }
    private static final Logger logger = LogManager.getLogger(Newtwitte.class);
    public int newComment(String username) {
        logger.info("System: user went to newComment");
        var submit = new submitTwitte();
        String response = JOptionPane.showInputDialog("Insert your comment");
        if (response==null) {
            return -1;
        }else{
            logger.info("System: Commented");
            return submit.Sub_twitte(response, username);
        }



    }


    public Newtwitte(String response,String username) throws Exception {
        logger.info("System: user went to twitteControl.Newtwitte");
        var submit = new submitTwitte();

        if (submit.Sub_twitte(response, username)!=0) {
            logger.info("System: twitted");
        } else {
            JOptionPane.showMessageDialog(null,"An error occurred, try again");
        }
    }



}
