package jsonContoller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import objects.objReport;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class jsonReports {
    public jsonReports() {
    }

    public jsonReports(List<objects.objReport> reps) {
        Gson RepObj = new GsonBuilder().setPrettyPrinting().create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("reports.Reports.json"));
            RepObj.toJson(reps, writer);
            writer.close();
        } catch (Exception ex) {

        }
    }
    List<objects.objReport> reports;
    public List<objects.objReport> get() {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("Reports.json"));
            reports = new Gson().fromJson(
                    reader, new TypeToken<List<objects.objReport>>() {
                    }.getType());
            reader.close();
        } catch (IOException ignored) {
        }
        return reports;
    }
}
