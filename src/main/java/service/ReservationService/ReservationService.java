package service.ReservationService;

import domain.BoatType;
import domain.ReservationRecord;
import service.BoatService.BoatService;

import java.util.ArrayList;

public interface ReservationService {
    public int getNewReservationNumber();
    public ArrayList<ReservationRecord> getReservations();
    public ReservationRecord getReservationWithReservationNumber(int reserveringsNummer);
    public void updateReservation(ReservationRecord updatedReservation);
    void addReservation(ReservationRecord newReservation);
    void deleteReservation(ReservationRecord Reservation);
    public int getBoatAmountAvailableDuringTimeByType (BoatType boatType, String startTimeString, String endTimeString, String dateString, BoatService boatService, ArrayList<String> boatIDs);

    String getFirstAvailableBoatOfType(BoatType selectedBoatType, ArrayList<String> boatIDs, BoatService boatService, String startTime, String endTime, String dateString);
}
