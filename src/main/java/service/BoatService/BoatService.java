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

    double getBoatPrice15FromName(String name) throws Exception;
    double getBoatPrice30FromName(String name) throws Exception;

    boolean isBoatBroken(String boatID);
}
