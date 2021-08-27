package jsonContoller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import objects.objUsers;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class jsonUsers {
    public jsonUsers() {
    }
    public jsonUsers(List<objects.objUsers> users) {
        Gson userObj = new GsonBuilder().setPrettyPrinting().create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("Users.json"));
            userObj.toJson(users, writer);
            writer.close();
        } catch (Exception ex) {

        }

    }
    List<objects.objUsers> users;


    public List<objects.objUsers> get() {
            try {
                Gson gson = new Gson();
                Reader reader = Files.newBufferedReader(Paths.get("Users.json"));
                users = new Gson().fromJson(
                        reader, new TypeToken<List<objects.objUsers>>() {
                        }.getType());
                reader.close();
            } catch (IOException ignored) {
            }
        return users;
    }
}
