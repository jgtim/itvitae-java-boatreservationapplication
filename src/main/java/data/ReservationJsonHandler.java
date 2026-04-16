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
    private ArrayList<ReservationDto> reservationDtos;

    public ReservationJsonHandler() {
        loadRepository();

    }

    private void loadRepository() {
        try(Reader fileReader = new FileReader(FILE_PATH.toString())) {
            Type userListType = new TypeToken<ArrayList<ReservationDto>>(){}.getType();
            reservationDtos = new Gson().fromJson(fileReader, userListType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<ReservationDto> getReservationList() {
        return reservationDtos;
    }

    @Override
    public void addReservation(ReservationDto reservationDto) {
        reservationDtos.add(reservationDto);
        saveReservationList();
    }

    @Override
    public void deleteReservation(ReservationDto reservationDto){
        reservationDtos.remove(reservationDto);
        saveReservationList();
    }

    private void saveReservationList() {
        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

        try(FileWriter fileWriter = new FileWriter(FILE_PATH.toString())) {
            gsonPretty.toJson(reservationDtos, fileWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
