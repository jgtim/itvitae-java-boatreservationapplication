package service.BoatStatusHandler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BoatStatusHandlerImpl implements BoatStatusHandler {
    private static final String FILE_PATH = "data/boat_status.json";
    private Gson gson = new Gson();

    /*
    Saving boat status isBroken method
    Using Map<boatName, isBroken>
     */
    public void saveBoatStatuses(Map<String, Boolean> statuses) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(statuses, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Loading boat statuses loadBoatStatuses method
    Using Map<boatName, isBroken
     */
    public Map<String, Boolean> loadStatuses() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new HashMap<>();

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<HashMap<String, Boolean>>() {
            }.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}
