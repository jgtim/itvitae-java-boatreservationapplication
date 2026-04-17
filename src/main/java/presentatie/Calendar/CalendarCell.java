package presentatie.Calendar;

public class CalendarCell {

    private final String BoatID;
    private boolean Available;
    private final String timeBox;
    private int reservationNumber;

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

    public int getReservationNumber(){
        return this.reservationNumber;
    }

    public boolean getAvailable(){
        return this.Available;
    }
    public void reserve(int reservationNumber){
        this.Available = false;
        this.reservationNumber = reservationNumber;
    }

    public String getTimeBox(){return this.timeBox;}
}

