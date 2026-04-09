package presentatie;

import data.ReservationJsonHandler;
import data.ReservationRepositoryHandler;
import service.ReservationService.ReservationService;
import service.ReservationService.ReservationServiceImpl;

public class Main {
    public static void main(String[] args) {
        ReservationRepositoryHandler reservationRepositoryHandler = new ReservationJsonHandler();
        ReservationService reservationService = new ReservationServiceImpl(reservationRepositoryHandler);
        new Tabs(reservationService);
    }
}