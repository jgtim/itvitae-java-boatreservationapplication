package presentatie.Calendar;

public class CalendarCell {

    private final String BoatID;
    private boolean Available;
    private final String timeBox;
    public final int reservationNumber;

    public CalendarCell(String boatID, String timeBox, boolean Available, int reservationNumber) {
        this.BoatID = boatID;
        this.Available = Available;
        this.timeBox = timeBox;
        this.reservationNumber = reservationNumber;
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

