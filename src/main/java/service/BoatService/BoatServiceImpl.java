package service.BoatService;

import domain.BoatRecord;

import java.util.ArrayList;

public class BoatServiceImpl implements BoatService {
    private ArrayList<BoatRecord> boatList;

    public BoatServiceImpl(ArrayList<BoatRecord> boatList) {
        this.boatList = boatList;
    }

    @Override
    public ArrayList<BoatRecord> getBoatList() {
        return boatList;
    }
}
