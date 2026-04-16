package presentatie;

import data.BoatDataExcel;
import data.BoatDataHandler;
import data.ReservationJsonHandler;
import data.ReservationRepositoryHandler;
import domain.BoatRecord;
import service.BoatService.BoatService;
import service.BoatService.BoatServiceImpl;
import service.BoatStatusHandler.BoatStatusHandler;
import service.BoatStatusHandler.BoatStatusHandlerImpl;
import service.ReservationService.ReservationService;
import service.ReservationService.ReservationServiceImpl;

import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Setting up Reservations Handler
        ReservationRepositoryHandler reservationRepositoryHandler = new ReservationJsonHandler();
        ReservationService reservationService = new ReservationServiceImpl(reservationRepositoryHandler);

        // Loading raw Boat Data from Excel and putting into list
        BoatDataHandler boatDataHandler = new BoatDataExcel();
        ArrayList<BoatRecord> boatsFromExcel = boatDataHandler.read();

        // 3. Loading isBroken Statuses from JSON
        BoatStatusHandler statusHandler = new BoatStatusHandlerImpl();
        Map<String, Boolean> brokenMap = statusHandler.loadStatuses();

        // Merging loaded isBroken statuses with raw Excel data List
        ArrayList<BoatRecord> finalizedBoats = new ArrayList<>();
        for (BoatRecord b : boatsFromExcel) {
            boolean isBroken = brokenMap.getOrDefault(b.boatName(), false);

            // Reconstruct the record with the correct status
            finalizedBoats.add(new BoatRecord(
                    b.boatType(), b.boatName(), b.lakeArea(), b.capacity(),
                    b.isElectric(), b.hasLifeJackets(), isBroken, b.price15(), b.price30()
            ));
        }

        // Initializing boatService with the new merged list
        BoatService boatService = new BoatServiceImpl(finalizedBoats);

        // Launching UI
        new Tabs(reservationService, boatService);
    }
}