package domain;

public enum LakeArea {
    // All supplied Lake Areas with String data on how to display it in GUI
    CENTRAL_LAKE("Centraal meer"),
    OPEN_LAKE("Open meer"),
    NEAR_SHORE("Bij de kust");

    private final String displayLakeArea;

    LakeArea(String displayLakeArea) {
        this.displayLakeArea = displayLakeArea;
    }

    public String getDisplayLakeArea() {
        return displayLakeArea;
    }
}
