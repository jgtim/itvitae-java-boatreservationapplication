package service.ReservationService;

import domain.ReservationRecord;

import java.util.ArrayList;

public interface ReservationService {
    public int getNewReservationNumber();
    public ArrayList<ReservationRecord> getReservations();
    public ReservationRecord getReservationWithReservationNumber(int reserveringsNummer);
    void addReservation(ReservationRecord newReservation);
}
