package domain;

import lombok.Builder;

import java.util.ArrayList;

@Builder(toBuilder = true)
public record ReservationRecord(
        int reserveringsNummer,
        CustomerRecord customerRecord,
        boolean betaald,
        boolean kamerFacturatie,
        String datum,
        String begintijd,
        String eindtijd,
        ArrayList<String> boatIDs,
        double prijs
) { }
