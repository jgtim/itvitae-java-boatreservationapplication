package service.ReservationService;

import domain.ReservationRecord;

import java.util.ArrayList;

public interface ReservationService {
    public int getNewReservationNumber();
    public ArrayList<ReservationRecord> getReservations();
    public ReservationRecord getReservationWithReservationNumber(int reserveringsNummer);
    public void updateReservation(ReservationRecord updatedReservation);
    void addReservation(ReservationRecord newReservation);
    void deleteReservation(ReservationRecord Reservation);
}
