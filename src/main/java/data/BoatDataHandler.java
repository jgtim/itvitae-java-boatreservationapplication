package data;

import domain.BoatRecord;
import service.Boat;

import java.util.ArrayList;

public interface BoatDataHandler {

    public ArrayList<Boat> read();
}
