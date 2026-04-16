package service.ReservationService;

import Exceptions.ReservationNotFoundException;
import data.ReservationRepositoryHandler;
import service.ReservationDto;

import java.util.ArrayList;

public class ReservationServiceImpl implements ReservationService {
    ReservationRepositoryHandler reservationRepositoryHandler;

    public ReservationServiceImpl(ReservationRepositoryHandler reservationRepositoryHandler) {
        this.reservationRepositoryHandler = reservationRepositoryHandler;
    }

    public int getNewReserveringsNummer() {
        int newNumber = 1;
        ArrayList<ReservationDto> reservationsList = reservationRepositoryHandler.getReservationList();
        if (!reservationsList.isEmpty()) {
            newNumber = reservationsList.getLast().getReservationRecord().reserveringsNummer() + 1;
        }
        return newNumber;
    }

    @Override
    public void addReservation(ReservationDto newReservation) {
        reservationRepositoryHandler.addReservation(newReservation);
    }

    public ArrayList<ReservationDto> getReservationList() {
        return reservationRepositoryHandler.getReservationList();
    }

    public ReservationDto getReservationWithReservationNumber(int reserveringsNummer) {
        for (ReservationDto reservationDto : reservationRepositoryHandler.getReservationList()) {
            if (reserveringsNummer == reservationDto.getReservationRecord().reserveringsNummer()) {
                return reservationDto;
            }
        }
        throw new ReservationNotFoundException("reserveringsNummer not found: " + reserveringsNummer);
    }
}
