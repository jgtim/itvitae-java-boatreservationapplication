package presentatie.Calendar;
import data.ReservationJsonHandler;
import java.time.LocalDate;
import java.util.HashMap;
import domain.ReservationRecord;
import service.ReservationService.ReservationService;

import java.util.*;

public class Calendar {
    private LocalDate CalendarDate;
    private HashMap<String, CalendarCell[]> CalendarGrid;
    // private Integer[] BoatIDs = {1, 2, 3}; //TODO placeholder data
    // Linked this field to CalendarDisplay class
    private String[] BoatIDs;
    public final String[] timeBoxes = {"09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15",
            "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15",
            "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15",
            "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15",
            "20:30", "20:45", "21:00", "21:15", "21:30"};
    private ReservationService reservationService;

    // Made constructor accept Integer[] BoatIDs
    public Calendar(ReservationService reservationService, LocalDate currentDate, String[] BoatIDs){
        this.reservationService = reservationService;
        this.CalendarDate = currentDate;
        this.CalendarGrid = new HashMap<String, CalendarCell[]>();
        this.BoatIDs = BoatIDs; // Added it here
        List<String> timeList = Arrays.asList(timeBoxes);
        ArrayList<ReservationRecord> reservationList = getReservationsForDate(this.CalendarDate);
        for(int i = 0; i < BoatIDs.length; i++){
            //50 calendar cells with boat ID
            ArrayList<ReservationRecord> boatReservations = getReservationsForBoat(reservationList, BoatIDs[i]);
            ArrayList<String> reservedTimes = new ArrayList<>();
            //this loop creates a list of all times in which a specific boat is reserved
            for(int r = 0; r < boatReservations.size(); r++){
                ReservationRecord record = boatReservations.get(r);
                String[] newTimes = Arrays.copyOfRange(timeBoxes, timeList.indexOf(record.begintijd()), timeList.indexOf(record.eindtijd()));
                Collections.addAll(reservedTimes, newTimes);

            }
            CalendarCell currentRow[] = new CalendarCell[50];
            for(int j = 0; j < 50; j++){
                boolean Available = !reservedTimes.contains(timeBoxes[j]);
                int reservationNumber = 0;
                //this finds the reservation number
                if(!Available){
                    for(int r = 0; r < boatReservations.size(); r++){
                        ReservationRecord record = boatReservations.get(r);
                        if(j >= timeList.indexOf(record.begintijd()) && j <= timeList.indexOf(record.eindtijd()) - 1){
                            reservationNumber = record.reserveringsNummer();
                            break;
                        };
                    }
                }
                currentRow[j] = new CalendarCell(BoatIDs[i], timeBoxes[j], Available, reservationNumber );
            }
            CalendarGrid.put(BoatIDs[i], currentRow);
        }
    }

//    public void showCalendar(){
//        for(int i = 0; i < CalendarGrid.size(); i++){
//            System.out.println("row " + (i+1));
//            for(int j = 0; j < CalendarGrid.get(BoatIDs[i]).length; j++){
//                System.out.println(CalendarGrid.get(BoatIDs[i])[j].showData());
//            }
//            System.out.println("----");
//        }
//    }

    public HashMap<String, CalendarCell[]> getCalendarGrid(){
        return this.CalendarGrid;
    }

    public LocalDate getCalendarDate(){return this.CalendarDate;}

    private ArrayList<ReservationRecord> getReservationsForDate(LocalDate date){
        ReservationJsonHandler reservationHandler = new ReservationJsonHandler();
        ArrayList<ReservationRecord> totalReservationList =  reservationService.getReservations();
        ArrayList<ReservationRecord> partialReservationList = new ArrayList<>();
        for(int i = 0; i < totalReservationList.size(); i++){
            if(Objects.equals(totalReservationList.get(i).datum(), date.toString())){
                partialReservationList.add(totalReservationList.get(i));
            }
        }
        return partialReservationList;
    }

    private ArrayList<ReservationRecord> getReservationsForBoat(ArrayList<ReservationRecord> reservationList, String BoatID) {
        ArrayList<ReservationRecord> partialReservationList = new ArrayList<>();
        for(int i = 0; i < reservationList.size(); i++){
            if(reservationList.get(i).boatIDs() != null && reservationList.get(i).boatIDs().contains(BoatID)){
                partialReservationList.add(reservationList.get(i));
            }
        }
        return partialReservationList;
    }
}
