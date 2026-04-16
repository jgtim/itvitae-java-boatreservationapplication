package data;

import service.ReservationDto;

import java.util.ArrayList;

public interface ReservationRepositoryHandler {
    public ArrayList<ReservationDto> getReservationList();
    public void addReservation(ReservationDto reservationDto);

    public void deleteReservation(ReservationDto reservationDto);
}
