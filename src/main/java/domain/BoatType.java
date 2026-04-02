package domain;

public enum BoatType {
    // All supplied Boat Types with String data on how to display it in GUI
    STANDARD_ROWBOAT("Standard Rowboat"),
    FAMILY_ROWBOAT("Family Rowboat"),
    KAYAK("1 Person Kayak"),
    PEDAL("Pedal Boat"),
    SILENT_ELECTRIC("Silent Electric Boat"),
    PREMIUM_ELECTRIC("Premium Electric Boat");

    private final String displayBoatType;

    BoatType(String displayBoatType) {
        this.displayBoatType = displayBoatType;
    }

    public String getDisplayBoatType() {
        return displayBoatType;
    }

}