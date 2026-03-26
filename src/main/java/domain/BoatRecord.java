package domain;

public record BoatRecord(
        int ID,
        String type,
        int capacity,
        LakeArea lakeArea,
        boolean electric,
        boolean hasLifeJackets,
        String comments,
        int pricePer15,
        int pricePer30) {
}
