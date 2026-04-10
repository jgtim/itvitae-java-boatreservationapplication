package domain;

import java.util.ArrayList;

public record ReservationRecord(int reserveringsNummer, CustomerRecord customerRecord, boolean betaald, boolean kamerFacturatie, String datum, String begintijd, String eindtijd, ArrayList<Integer> boatIDs) {


    @Override
    public String toString() {
        return ""
                + customerRecord + ", "
                + betaald + ", "
                + kamerFacturatie + ", "
                + datum + ", " + begintijd
                + ", " + eindtijd
                + ", "
                + ((boatIDs != null) ? boatIDs.toString().replace(",", "."): boatIDs);
    }
}
