package service.BoatService;

import domain.BoatRecord;

import java.util.ArrayList;

public interface BoatService {
    public ArrayList<BoatRecord> getBoatList();
    void setBoatBroken(String boatName, boolean isBroken);
}
