package presentatie.Calendar;

public class CalendarCell {

    private final Integer BoatID;
    private boolean Available;
    private final String timeBox;

    public CalendarCell(Integer boatID, String timeBox, boolean Available) {
        this.BoatID = boatID;
        this.Available = Available;
        this.timeBox = timeBox;
    }

    public String showData(){
        return "Boat ID: " + this.BoatID + " availibility: " + this.Available + " time: " + this.timeBox;
    }

    public Integer getBoatID(){
        return this.BoatID;
    }

    public boolean getAvailable(){
        return this.Available;
    }
    public void reserve(){
        this.Available = false;
    }

    public String getTimeBox(){return this.timeBox;}
}

