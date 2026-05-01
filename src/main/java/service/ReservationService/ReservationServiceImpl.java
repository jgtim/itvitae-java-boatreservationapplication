package service.ReservationService;

import Exceptions.ReservationNotFoundException;
import data.ReservationRepositoryHandler;
import domain.BoatRecord;
import domain.BoatType;
import domain.ReservationRecord;
import service.BoatService.BoatService;

import java.time.LocalTime;
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

    private boolean isBoatAvailableDuringTime(String boatID, String startTimeString, String endTimeString, String dateString) {
        LocalTime startTimeToCheck = LocalTime.parse(startTimeString);
        LocalTime endTimeToCheck = LocalTime.parse(endTimeString);

        for (ReservationRecord reservationRecord : reservationRecords) {
            if (reservationRecord.datum().equals(dateString) && reservationRecord.boatIDs().contains(boatID)) {
                LocalTime reservationStartTime = LocalTime.parse(reservationRecord.begintijd());
                LocalTime reservationEndTime = LocalTime.parse(reservationRecord.eindtijd());

                if (startTimeToCheck.isBefore(reservationEndTime) && endTimeToCheck.isAfter(reservationStartTime)) {
                    return false;
                }
            }
        }

        return true;
    }

    public int getBoatAmountAvailableDuringTimeByType (BoatType boatType, String startTimeString, String endTimeString, String dateString, BoatService boatService, ArrayList<String> boatIDs) {
        int total = boatService.getBoatAmountOfType(boatType);
        var subtractedBoats = new ArrayList<String>();

        for (BoatRecord boatRecord : boatService.getBoatList()) {
            if (boatRecord.boatType() == boatType && boatIDs.contains(boatRecord.boatName())) {
                if (!subtractedBoats.contains(boatRecord.boatName())) {
                    total -= 1;
                    subtractedBoats.add(boatRecord.boatName());
                }
            }

        }

        for (ReservationRecord reservationRecord : reservationRecords) {
            for (String boatID : reservationRecord.boatIDs()) {
                if (boatType == boatService.getBoatTypeFromID(boatID)) {
                    if (!isBoatAvailableDuringTime(boatID, startTimeString, endTimeString, dateString)) {
                        if (!subtractedBoats.contains(boatID)) {
                            total -= 1;
                            subtractedBoats.add(boatID);
                        }
                    }
                }
            }
        }
        return total;
    }

    @Override
    public int getBoatAmountAvailableDuringTimeByType(BoatType boatType, String startTimeString, String endTimeString, String dateString, BoatService boatService, ArrayList<String> boatIDs, ReservationRecord reservationToExclude) {
        ArrayList<ReservationRecord> reservationRecordsStash = (ArrayList<ReservationRecord>) reservationRecords.clone();
        reservationRecords.remove(reservationToExclude);
        int result =  getBoatAmountAvailableDuringTimeByType(boatType, startTimeString, endTimeString, dateString, boatService, boatIDs);
        reservationRecords = reservationRecordsStash;
        return result;
    }

    @Override
    public String getFirstAvailableBoatOfType(BoatType selectedBoatType, ArrayList<String> boatIDs, BoatService boatService, String startTime, String endTime, String dateString) {
        ArrayList<BoatRecord> boatList = boatService.getBoatList();
        for (BoatRecord boatRecord : boatList) {
            if (boatRecord.boatType() == selectedBoatType && !boatIDs.contains(boatRecord.boatName())) {
                if (isBoatAvailableDuringTime(boatRecord.boatName(), startTime, endTime, dateString) && !boatRecord.isBroken()) {
                    return boatRecord.boatName();
                }
            }
        }
        return null;
    }

    @Override
    public boolean isBoatIDOverLapping(String boatID, String date, String startTime, String endTime, int reservationNumber) {
        LocalTime startTimeToCheck = LocalTime.parse(startTime);
        LocalTime endTimeToCheck = LocalTime.parse(endTime);

        for (ReservationRecord reservationRecord : reservationRecords) {
            LocalTime reservationStartTime = LocalTime.parse(reservationRecord.begintijd());
            LocalTime reservationEndTime = LocalTime.parse(reservationRecord.eindtijd());
            if(
                    reservationNumber != reservationRecord.reserveringsNummer()
                    && reservationRecord.boatIDs().contains(boatID)
                    && date.equals(reservationRecord.datum())
                    && startTimeToCheck.isBefore(reservationEndTime) && endTimeToCheck.isAfter(reservationStartTime)
            ) {
                return true;
            }
        }

        return false;
    }

}
