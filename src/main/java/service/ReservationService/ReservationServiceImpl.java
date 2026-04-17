package service.ReservationService;

import Exceptions.ReservationNotFoundException;
import data.ReservationRepositoryHandler;
import domain.ReservationRecord;

import java.util.ArrayList;

public class ReservationServiceImpl implements ReservationService {
    ArrayList<ReservationRecord> reservationRecords = new ArrayList<ReservationRecord>();

    ReservationRepositoryHandler reservationRepositoryHandler;

    public ReservationServiceImpl(ReservationRepositoryHandler reservationRepositoryHandler) {
        this.reservationRepositoryHandler = reservationRepositoryHandler;
        loadReservationsFromRepository();
    }

    public int getNewReservationNumber() {
        int newNumber = 1;
        if (!reservationRecords.isEmpty()) {
            newNumber = reservationRecords.getLast().reserveringsNummer() + 1;
        }
        return newNumber;
    }

    @Override
    public void addReservation(ReservationRecord newReservation) {
        reservationRecords.add(newReservation);
        saveReservationsToRepository();
    }

    @Override
    public void deleteReservation(ReservationRecord Reservation){
        reservationRecords.remove(Reservation);
        saveReservationsToRepository();
    }

    public ArrayList<ReservationRecord> getReservations() {
        return reservationRecords;
    }

    public ReservationRecord getReservationWithReservationNumber(int reserveringsNummer) {
        for (ReservationRecord reservationRecord: reservationRecords) {
            if (reserveringsNummer == reservationRecord.reserveringsNummer()) {
                return reservationRecord;
            }
        }
        throw new ReservationNotFoundException("reserveringsNummer not found: " + reserveringsNummer);
    }

    private void loadReservationsFromRepository() {
        reservationRecords = reservationRepositoryHandler.getReservationRecords();
    }

    private void saveReservationsToRepository() {
        reservationRepositoryHandler.saveReservationRecords(reservationRecords);
    }

    public void updateReservation(ReservationRecord updatedReservation) {
        int reservationNumber = updatedReservation.reserveringsNummer();
        for (int i = 0; i < reservationRecords.size() ; i++) {
                if (reservationRecords.get(i).reserveringsNummer() == reservationNumber) {
                    reservationRecords.set(i, updatedReservation);
                }
        }
        saveReservationsToRepository();
    }

}
