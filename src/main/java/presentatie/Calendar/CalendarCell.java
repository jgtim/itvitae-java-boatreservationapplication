package presentatie.Calendar;

public class CalendarCell {

    private final String BoatID;
    private boolean Available;
    private final String timeBox;

    public CalendarCell(String boatID, String timeBox) {
        this.BoatID = boatID;
        this.Available = true; //TODO make this false based on reservations data
        this.timeBox = timeBox;
    }

    public String showData(){
        return "Boat ID: " + this.BoatID + " availibility: " + this.Available + " time: " + this.timeBox;
    }

    public String getBoatID(){
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

