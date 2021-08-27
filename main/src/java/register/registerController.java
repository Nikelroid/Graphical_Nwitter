package register;

import jsonContoller.jsonUsers;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class registerController {


    public registerController() {
    }

    private final boolean register_status = false;
    private static final Logger logger = LogManager.getLogger(registerController.class);
    public void Registeruser() throws Exception {
        logger.info("System: user went to Register");
        new registerView();


    }

    public static boolean Checker(String pass , String username) {
        File f = new File("Users.json");
        if (f.exists()) {
            var get_j = new jsonUsers();
            List<objUsers> users = get_j.get();
            for (int i = 0; i < users.size(); i++)
                if (users.get(i).getUsername().equals(username)&&
                        users.get(i).getPassword().equals(pass))
                    return true;
            }
        return false;
        }


}

