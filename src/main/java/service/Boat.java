package service;

// Public Class Boat. Needs to be connected to database later and already linked to Boat Tab
public class Boat {
    private final String name;
    private final int capacity;
    private final String type;

    // 'may be final'. Owner Molveno Boat Rentals MAY need to be able to edit later: added features
    private String features;

    // 'may be final'. Owner Molveno Boat Rentals needs to be able to personally adapt this later through database.
    private double price15;
    private double price30;

    // Full Constructor: used when we know the specific details from database
    public Boat(String name, int capacity, double p15, double p30, String type, String features) {
        this.name = name;
        this.capacity = capacity;
        this.price15 = p15;
        this.price30 = p30;
        this.type = type;
        this.features = features;
    }

    // Default Constructor: calls the Full Constructor with standard values; for testing
    public Boat() {
        this("Nieuwe Boot", 4, 15.0, 25.0, "Sloep", "Zonnedek");
    }




    // --- GETTERS ---
    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPrice15() {
        return price15;
    }

    public double getPrice30() {
        return price30;
    }

    public String getType() {
        return type;
    }

    public String getFeatures() {
        return features;
    }
}



// --- SETTERS NEEDED? ONLY WAY TO CHANGE PRICE (and maybe features) ARE THROUGH DATABASE BY OWNER MOLVENO