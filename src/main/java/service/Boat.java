package service;

import domain.BoatType;
import domain.LakeArea;

// Public Class Boat. Needs to be connected to database later
// Is already linked to BoatTab
public class Boat {

    private final BoatType boatType;
    private final String boatTypeMember;

    private final LakeArea lakeArea;
    private final int capacity;

    private final boolean isElectric;
    private final boolean hasLifeJacket;

    // 'may be final'. Owner Molveno Boat Rentals needs to be able to personally adapt this later through database.
    private double price15;
    private double price30;

    private final int id;

    // Full Constructor: using specific details from database
    public Boat(BoatType boatType, String boatTypeMember, LakeArea lakeArea,
                int capacity,
                boolean isElectric, boolean hasLifeJacket,
                double p15, double p30,
                int id) {

        this.boatType = boatType;
        this.boatTypeMember = boatTypeMember;
        this.lakeArea = lakeArea;

        this.capacity = capacity;

        this.isElectric = isElectric;
        this.hasLifeJacket = hasLifeJacket;

        this.price15 = p15;
        this.price30 = p30;

        this.id = id;
    }

    // Default Constructor: calls the Full Constructor with standard values; for testing
    public Boat() {
        this(BoatType.STANDARD_ROWBOAT, "Rowboat 1", LakeArea.CENTRAL_LAKE, 2 ,false, true, 12.5, 22.5, 1);
    }


    // --- GETTERS ---
    public BoatType getBoatType() {
        return boatType;
    }

    public String getBoatTypeMember() {
        return boatTypeMember;
    }

    public LakeArea getLakeArea() {
        return lakeArea;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public boolean hasLifeJacket() {
        return hasLifeJacket;
    }

    public double getPrice15() {
        return price15;
    }

    public double getPrice30() {
        return price30;
    }

    public int getId(){
        return id;
    }
}

// --- SETTERS NEEDED? ONLY WAY TO CHANGE PRICE (and maybe features) ARE THROUGH DATABASE BY OWNER MOLVENO