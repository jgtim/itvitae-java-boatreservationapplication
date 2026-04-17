package service.BoatService;

import domain.BoatRecord;
import service.BoatStatusHandler.BoatStatusHandler;
import service.BoatStatusHandler.BoatStatusHandlerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps track of a list containing all boats.
 */
public class BoatServiceImpl implements BoatService {
    private ArrayList<BoatRecord> boatList;
    private BoatStatusHandler boatStatusHandler =  new BoatStatusHandlerImpl();

    /**
     * Constructor accepts a list of all boat data.
     * @param boatList ArrayList containing the data of all boats
     */
    public BoatServiceImpl(ArrayList<BoatRecord> boatList) {

        this.boatList = boatList;
    }

    /**
     * Get the list of boat data.
     * @return ArrayList containing the data of all boats
     */
    @Override
    public ArrayList<BoatRecord> getBoatList() {

        return boatList;
    }

    public void setBoatBroken(String boatName, boolean isBroken) {
        for (int i = 0; i < boatList.size(); i++) {
            BoatRecord oldBoat = boatList.get(i); // storing every created Boat Object in oldBoat

            if (oldBoat.boatName().equals(boatName)) {// if supplied boatName matches one in the boatList, create a new boat Object and remove old one
                BoatRecord updatedBoat = new BoatRecord(
                        oldBoat.boatType(),
                        oldBoat.boatName(),
                        oldBoat.lakeArea(),
                        oldBoat.capacity(),
                        oldBoat.isElectric(),
                        oldBoat.hasLifeJackets(),
                        isBroken,
                        oldBoat.price15(),
                        oldBoat.price30()
                );
                boatList.set(i, updatedBoat);

                saveAllStatuses();
                return;
            }
        }
    }

    private void saveAllStatuses() {
        Map<String, Boolean> statuses = new HashMap<>();
        for (BoatRecord b : boatList) {
            statuses.put(b.boatName(), b.isBroken());
        }
        boatStatusHandler.saveBoatStatuses(statuses);
    }

}
