package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import service.ReservationDto;

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
    public ArrayList<ReservationDto> getReservationList() {
        try(Reader fileReader = new FileReader(FILE_PATH.toString())) {
            Type userListType = new TypeToken<ArrayList<ReservationDto>>(){}.getType();
            ArrayList<ReservationDto> result = new Gson().fromJson(fileReader, userListType);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addReservation(ReservationDto reservationDto) {
        var reservationList = getReservationList();
        reservationList.add(reservationDto);
        saveReservationList(reservationList);
    }

    private void saveReservationList(ArrayList<ReservationDto> reservationList) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try(FileWriter fileWriter = new FileWriter(FILE_PATH.toString())) {
            gson.toJson(reservationList, fileWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
