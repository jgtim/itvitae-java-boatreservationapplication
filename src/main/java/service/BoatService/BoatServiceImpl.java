package service.BoatService;

import domain.BoatRecord;
import domain.BoatType;

import java.util.ArrayList;

/**
 * This class keeps track of a list containing all boats.
 */
public class BoatServiceImpl implements BoatService {
    private ArrayList<BoatRecord> boatList;

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

                // Here we should add a DataHandler to save the updated boat status
                // Need to create a save() method
                return;
            }
        }
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


}
