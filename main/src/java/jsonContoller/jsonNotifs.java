package jsonContoller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import objects.objNotifs;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class jsonNotifs {
    public jsonNotifs() {
    }
    public jsonNotifs(List<objects.objNotifs> users) {
        Gson userObj = new GsonBuilder().setPrettyPrinting().create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("Notifs.json"));
            userObj.toJson(users, writer);
            writer.close();
        } catch (Exception ex) {

        }
    }
    List<objects.objNotifs> notifs;
    public List<objects.objNotifs> get() {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("Notifs.json"));
            notifs = new Gson().fromJson(
                    reader, new TypeToken<List<objects.objNotifs>>() {
                    }.getType());
            reader.close();
        } catch (IOException ignored) {
        }
        return notifs;
    }
}
