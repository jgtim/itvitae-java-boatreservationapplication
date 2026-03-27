package service;

import domain.CustomerRecord;
import domain.ReservationRecord;

public class Reservation {
    private ReservationRecord reservationRecord;

    public Reservation(String naam, int telefoonNummer, String email, boolean kamerFacturatie, boolean betaald) {
        reservationRecord = new ReservationRecord(new CustomerRecord(naam, telefoonNummer, email), kamerFacturatie, betaald);
    }

    @Override
    public String toString() {
        return reservationRecord.toString();
    }
}
