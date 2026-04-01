import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class Calendar {
    private String CalendarDate;
    private HashMap<String, CalendarCell[]> CalendarGrid;
    private String[] BoatIDs = {"Boat1", "Boat2", "Boat3"}; //placeholder data
    private String[] timeBoxes = {"9:00", "9:15", "9:30", "9:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15",
            "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15",
            "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15",
            "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15",
            "20:30", "20:45", "21:00", "21:15"};



    Calendar(String currentDate){
        this.CalendarDate = currentDate;
        this.CalendarGrid = new HashMap<String, CalendarCell[]>();

        for(int i = 0; i < BoatIDs.length; i++){
            //50 calendar cells with boat ID
            CalendarCell currentRow[] = new CalendarCell[50];
            for(int j = 0; j < 50; j++){
                currentRow[j] = new CalendarCell(BoatIDs[i], timeBoxes[j]);
            }
            CalendarGrid.put(BoatIDs[i], currentRow);
        }
    }

    public void showCalendar(){
        for(int i = 0; i < CalendarGrid.size(); i++){
            System.out.println("row " + (i+1));
            for(int j = 0; j < CalendarGrid.get(BoatIDs[i]).length; j++){
                System.out.println(CalendarGrid.get(BoatIDs[i])[j].showData());
            }
            System.out.println("----");
        }
    }

    public HashMap<String, CalendarCell[]> getCalendarGrid(){
        return this.CalendarGrid;
    }
}
