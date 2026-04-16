package service.ReservationService;

import service.ReservationDto;

import java.util.ArrayList;

public interface ReservationService {
    public int getNewReserveringsNummer();
    public ArrayList<ReservationDto> getReservationList();
    public ReservationDto getReservationWithReservationNumber(int reserveringsNummer);
    void addReservation(ReservationDto newReservation);

    void deleteReservation(ReservationDto reservationDto);
}
