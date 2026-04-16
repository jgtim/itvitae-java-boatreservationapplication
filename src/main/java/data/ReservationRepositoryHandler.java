package data;

import domain.ReservationRecord;

import java.util.ArrayList;

public interface ReservationRepositoryHandler {
    ArrayList<ReservationRecord> getReservationRecords();
    public void saveReservationRecords(ArrayList<ReservationRecord> reservationRecords);
}
