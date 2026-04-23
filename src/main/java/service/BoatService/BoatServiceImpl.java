package service.BoatService;

import domain.BoatRecord;
import service.BoatStatusHandler.BoatStatusHandler;
import service.BoatStatusHandler.BoatStatusHandlerImpl;
import domain.BoatType;

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


    public int getBoatAmountOfType(BoatType boatType) {
        int result = 0;
        for (BoatRecord boatRecord : boatList) {
            if (boatRecord.boatType() == boatType) {
                result += 1;
            }
        }
        return result;
    }

    @Override
    public int getBoatCapacityFromType(BoatType boatType) {
        for (BoatRecord boatRecord : boatList) {
            if (boatRecord.boatType() == boatType) {
                return boatRecord.capacity();
            }
        }
        return 0;
    }

    @Override
    public BoatType getBoatTypeFromID(String boatID) {
        for (BoatRecord boatRecord : boatList) {
            if (boatRecord.boatName().equals(boatID)) {
                return boatRecord.boatType();
            }
        }
        return null;
    }

    @Override
    public double getBoatPrice15FromName(String boatName) throws Exception {
        for (BoatRecord boatRecord : boatList) {
            if (boatRecord.boatName().equals(boatName)) {
                return boatRecord.price15();
            }
        }
        throw new Exception("Price15 not found");
    }

    @Override
    public double getBoatPrice30FromName(String boatName) throws Exception {
        for (BoatRecord boatRecord : boatList) {
            if (boatRecord.boatName().equals(boatName)) {
                return boatRecord.price30();
            }
        }
        throw new Exception("Price30 not found");
    }


}
