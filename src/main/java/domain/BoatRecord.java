package domain;

public record BoatRecord(
        BoatType boatType,
        String boatName,
        LakeArea lakeArea,
        int capacity,
        boolean isElectric,
        boolean hasLifeJackets,
        double price15,
        double price30) {
}
