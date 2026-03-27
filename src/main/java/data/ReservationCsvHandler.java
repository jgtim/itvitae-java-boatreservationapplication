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
import java.util.Objects;

public class ReservationCsvHandler {

    private final Path FILE_PATH = Paths.get("data/ReservationRepository.csv");

    public ArrayList<ReservationDto> getReservationList() {
        ArrayList<ReservationDto> result = new ArrayList<ReservationDto>();
        for (String line : getLines()) {
            String[] splitline = line.split(", ");
            result.add(new ReservationDto(
                    splitline[0],
                    (Objects.equals(splitline[1], "null")) ? null : Integer.parseInt(splitline[1]),
                    (Objects.equals(splitline[2], "null")) ? null : splitline[2],
                    Boolean.parseBoolean(splitline[3]),
                    Boolean.parseBoolean(splitline[4])
            ));
        }
        return result;
    }

    public ArrayList<String> getLines() {
        ArrayList<String> result = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH.toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                result.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        result.removeFirst();
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
