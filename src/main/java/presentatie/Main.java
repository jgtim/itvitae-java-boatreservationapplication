package presentatie;

import data.BoatDataExcel;
import data.BoatDataHandler;
import data.ReservationJsonHandler;
import data.ReservationRepositoryHandler;
import service.BoatService.BoatService;
import service.BoatService.BoatServiceImpl;
import service.ReservationService.ReservationService;
import service.ReservationService.ReservationServiceImpl;

public class Main {
    public static void main(String[] args) {
        ReservationRepositoryHandler reservationRepositoryHandler = new ReservationJsonHandler();
        ReservationService reservationService = new ReservationServiceImpl(reservationRepositoryHandler);
        BoatDataHandler boatDataHandler = new BoatDataExcel();
        BoatService boatService = new BoatServiceImpl(boatDataHandler.read());
        new Tabs(reservationService, boatService);
    }
}