package service.BoatService;

import domain.BoatRecord;
import domain.BoatType;

import java.util.ArrayList;

public interface BoatService {
    public ArrayList<BoatRecord> getBoatList();
    void setBoatBroken(String boatName, boolean isBroken);
    public int getBoatAmountOfType(BoatType boatType);
    public int getBoatCapacityFromType(BoatType boatType);
    public BoatType getBoatTypeFromID(String boatID);
}
