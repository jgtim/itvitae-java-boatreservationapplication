package service.ReservationService;

import service.ReservationDto;

import java.util.ArrayList;

public interface ReservationService {
    /**
     * @return ArrayList&ltReservationDto&gt
     */
    ArrayList<ReservationDto> getReservationList();
}
