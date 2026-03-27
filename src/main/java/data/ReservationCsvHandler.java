package data;

import service.ReservationDto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class ReservationCsvHandler {

    private final Path FILE_PATH = Paths.get("data/ReservationRepository.csv");

    public ArrayList<ReservationDto> getReservationList() {
        ArrayList<ReservationDto> result = new ArrayList<ReservationDto>();
        return result;
    }

    public ArrayList<String> getLines() {
        ArrayList<String> result = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader("ReservationRepository.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                result.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public void addReservation(ReservationDto reservationDto) {
        try {
            Files.writeString(FILE_PATH, "\n" + reservationDto.toString(), StandardOpenOption.APPEND);
            System.out.println("Added Reservation to ReservationRepository.csv: \n" + reservationDto.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
