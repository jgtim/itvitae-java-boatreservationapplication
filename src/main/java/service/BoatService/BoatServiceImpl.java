package service.BoatService;

import domain.BoatRecord;

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
}
