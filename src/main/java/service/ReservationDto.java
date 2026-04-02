package service;

import domain.CustomerRecord;
import domain.ReservationRecord;

public class ReservationDto {
    private ReservationRecord reservationRecord;

    public ReservationDto(String naam, Integer telefoonNummer, String email, boolean kamerFacturatie, boolean betaald, String datum, String begintijd, String eindtijd) {
        reservationRecord = new ReservationRecord(new CustomerRecord(naam, telefoonNummer, email), kamerFacturatie, betaald, datum, begintijd, eindtijd);
    }

    @Override
    public String toString() {
        return reservationRecord.toString();
    }
}
