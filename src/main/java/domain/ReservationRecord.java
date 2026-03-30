package domain;

public record ReservationRecord(CustomerRecord customerRecord, boolean betaald, boolean kamerFacturatie) {


    @Override
    public String toString() {
        return "" + customerRecord + ", " + betaald + ", " + kamerFacturatie;
    }
}
