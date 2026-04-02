package domain;

public enum LakeArea {
    // All supplied Lake Areas with String data on how to display it in GUI
    CENTRAL_LAKE("Central Lake"),
    OPEN_LAKE("Open Lake"),
    NEAR_SHORE("Near shore");

    private final String displayLakeArea;

    LakeArea(String displayLakeArea) {
        this.displayLakeArea = displayLakeArea;
    }

    public String getDisplayLakeArea() {
        return displayLakeArea;
    }
}
