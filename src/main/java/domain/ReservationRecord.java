package domain;

public record ReservationRecord(CustomerRecord customerRecord, boolean betaald, boolean kamerFacturatie, String datum, String begintijd, String eindtijd) {


    @Override
    public String toString() {
        return "" + customerRecord + ", " + betaald + ", " + kamerFacturatie + ", " + datum + ", " + begintijd + ", " + eindtijd;
    }
}
