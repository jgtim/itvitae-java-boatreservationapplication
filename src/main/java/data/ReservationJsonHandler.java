package data;

import Exceptions.RepositoryNotLoadedException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import domain.ReservationRecord;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ReservationJsonHandler implements ReservationRepositoryHandler{
    private final Path FILE_PATH = Paths.get("data/ReservationRepository.json");

    @Override
    public ArrayList<ReservationRecord> getReservationRecords() {
        try(Reader fileReader = new FileReader(FILE_PATH.toString())) {
            Type userListType = new TypeToken<ArrayList<ReservationRecord>>(){}.getType();
            return new Gson().fromJson(fileReader, userListType);
        } catch (Exception e) {
            throw new RepositoryNotLoadedException("Repository didn't load");
        }
    }

    @Override
    public void saveReservationRecords(ArrayList<ReservationRecord> reservationRecords) {
        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

        try(FileWriter fileWriter = new FileWriter(FILE_PATH.toString())) {
            gsonPretty.toJson(reservationRecords, fileWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
