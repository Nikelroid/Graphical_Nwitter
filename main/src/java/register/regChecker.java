package register;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import login.loginView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitRegister;

import javax.swing.*;

public class regChecker {
    static final Logger logger = LogManager.getLogger(regChecker.class);
    public static String[] regInfo = new String[11];
    TextField[] fields=new TextField[11];
    
    public regChecker() throws Exception {
        int regchecked = 0;
        {
            if (registerView.fields[0].getText().isEmpty()) {
                errorSetter(0,"Name can't be empty");
                return;
            } else {
                regInfo[0] = registerView.fields[0].getText();
                regInfo[1] = registerView.fields[1].getText();
            }
            if (registerView.fields[2].getText().isEmpty()) {
                errorSetter(2,"Username can't be empty");
                return;
            } else if (registerView.fields[2].getText().length() < 3) {
                errorSetter(2,"Username is too short");
                return;
            } else if (registerView.Checker(registerView.fields[2].getText(),1)) {
                errorSetter(2,"Username already chosen");
                return;
            } else {
                regInfo[2] = registerView.fields[2].getText();
            }
            if (registerView.fields[3].getText().isEmpty()) {
                errorSetter(3,"Password can't be empty");
                return;

            }else if (!registerView.fields[3].getText().equals(registerView.fields[4].getText())) {
                errorSetter(3,"Password and Confirm password don't match");
                registerView.fields[4].getStyleClass().add("wrong");
                return;
            } else if (registerView.fields[3].getText().length() < 7) {
                errorSetter(3,"Password is too short");
                return;
            } else if (!registerView.intchecker(registerView.fields[3].getText())) {
                errorSetter(3,"Password must contains letter or symbol");
                return;
            } else if (registerView.isstrong(registerView.fields[3].getText())) {
                errorSetter(3,"Password must contains a lowercase and uppercase letter an least");
                return;
            } else {
                regInfo[3] = registerView.fields[3].getText();
            }

            if (registerView.intchecker(registerView.fields[5].getText())) {
                errorSetter(5,"Year must be a number");
                return;
            } else if (Integer.parseInt(registerView.fields[5].getText()) > 2003 ||
                    Integer.parseInt(registerView.fields[5].getText()) < 1921) {
                errorSetter(5,"Year must be a number between 1921 to 2003");
                return;
            } else if (registerView.intchecker(registerView.fields[6].getText())) {
                errorSetter(6,"mounth must be a number");
                return;
            } else if (Integer.parseInt(registerView.fields[6].getText()) > 12 ||
                    Integer.parseInt(registerView.fields[6].getText()) < 1) {
                errorSetter(6,"Mounth must be a number between 1 to 12");
                return;
            } else if (registerView.intchecker(registerView.fields[7].getText())) {
                errorSetter(7,"Day must be a number");
                return;
            } else if (Integer.parseInt(registerView.fields[7].getText()) > 31 ||
                    Integer.parseInt(registerView.fields[7].getText()) < 1) {
                errorSetter(7,"Day must be a number between 1 and 31");
                return;
            } else {
                regInfo[7] = registerView.fields[5].getText();
                regInfo[6] = registerView.fields[6].getText();
                regInfo[5] = registerView.fields[7].getText();
            }

            if (registerView.fields[8].getText().isEmpty()) {
                errorSetter(8,"Email can't be empty");
                return;
            } else if (registerView.Checker(registerView.fields[8].getText(),2)) {
                errorSetter(8,"This Email registered before");
                return;
            } else {
                regInfo[8] = registerView.fields[8].getText();
            }

            if (registerView.fields[9].getText().isEmpty()) {
                regInfo[9] = "";
            } else if (registerView.intchecker(registerView.fields[9].getText())) {
                errorSetter(9,"Phonenumber must be a number (or empty)");
                return;
            } else if (registerView.Checker(registerView.fields[9].getText(),3)) {
                errorSetter(9,"This Phonenumber registered before");
                return;
            } else {
                regInfo[9] = registerView.fields[9].getText();
            }
            if (registerView.fields[10].getText().isEmpty())
                regInfo[10] = "";
            else
                regInfo[10] = registerView.fields[10].getText();
        }

            var submit = new submitRegister();
            if (submit.Sub_regester(regInfo)) {
                logger.info("System: Registered");
                JOptionPane.showMessageDialog(
                        null, "Registered successfully");
            } else {
                logger.error("System: Error in registering");
                JOptionPane.showMessageDialog(
                        null, "Error in registering");
            }
            new loginView(true);

    }

    public void errorSetter(int i , String errTxt){
            registerView.info.setText(errTxt);
            registerView.info.setTextFill(Color.RED);
            registerView.fields[i].getStyleClass().add("wrong");
            registerView.Normalizer(registerView.fields, registerView.info);
        }
}
