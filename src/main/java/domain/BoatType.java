package domain;

public enum BoatType {
    // All supplied Boat Types with String data on how to display it in GUI
    STANDARD_ROWBOAT("Standaard Roeiboot"),
    FAMILY_ROWBOAT("Familie Roeiboot"),
    KAYAK("1 Persoons Kayak"),
    PEDAL("Waterfiets"),
    SILENT_ELECTRIC("Stille Elektrische Boot"),
    PREMIUM_ELECTRIC("Premium Elektrische Boot");

    private final String displayBoatType;

    BoatType(String displayBoatType) {
        this.displayBoatType = displayBoatType;
    }

    public String getDisplayBoatType() {
        return displayBoatType;
    }

}