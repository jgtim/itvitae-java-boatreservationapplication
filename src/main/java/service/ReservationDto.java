package service;

import domain.CustomerRecord;
import domain.ReservationRecord;

import java.util.ArrayList;

public class ReservationDto {
    private ReservationRecord reservationRecord;

    public ReservationDto(String naam, Integer telefoonNummer, String email, boolean kamerFacturatie, boolean betaald, String datum, String begintijd, String eindtijd, ArrayList<Integer> boatIDs) {
        reservationRecord = new ReservationRecord(new CustomerRecord(naam, telefoonNummer, email), kamerFacturatie, betaald, datum, begintijd, eindtijd, boatIDs);
    }

    public ReservationRecord getReservationRecord() {
        return reservationRecord;
    }

    @Override
    public String toString() {
        return reservationRecord.toString();
    }
}
