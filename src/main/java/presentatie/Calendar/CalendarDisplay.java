package presentatie.Calendar;

import com.github.lgooddatepicker.components.DatePicker;
import data.BoatDataExcel;
import data.ReservationRepositoryHandler;
import domain.BoatRecord;
import domain.MolvenoColor;
import presentatie.ReserveringPopup;
import service.ReservationService.ReservationService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CalendarDisplay extends JPanel {
    private Calendar Calendar;
    private JPanel DisplayPanel = new JPanel();
    private Integer[] BoatIDs; //TODO placeholder data; maybe let user input selection of them
    private JButton[][] allCells;
    private Integer[] selectedStart = {null, null};
    private Integer[] selectedEnd = {null, null};
    private String[] timePoints = {"9:00", "9:15", "9:30", "9:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15",
            "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15",
            "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15",
            "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15",
            "20:30", "20:45", "21:00", "21:15", "21:30"};
    private LocalDate dateSelected;

    public CalendarDisplay(ReservationService reservationService) {
        // Getting Boat Records from BoatDataExcel
        var boatDataHandler = new BoatDataExcel();
        ArrayList<BoatRecord> allBoats = boatDataHandler.read();

        this.BoatIDs = allBoats.stream()
                .map(BoatRecord::id)
                .toArray(Integer[]::new);

        BoxLayout boxLayoutManager = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayoutManager);

        var datePicker = new DatePicker();
        datePicker.setMaximumSize(new Dimension(200, 25));
        datePicker.setDateToToday();
        add(datePicker);

        dateSelected = datePicker.getDate();

        // Giving the Integer[] BoatIDs to the Calendar constructor
        this.Calendar = new Calendar(dateSelected, this.BoatIDs);

        this.allCells = new JButton[BoatIDs.length][50];

        //the calendar grid ties a boat ID to a list of calendar cell objects, which contain data about that time point for that boat
        HashMap<Integer, CalendarCell[]> CalGrid = Calendar.getCalendarGrid();
        //this for loop creates the buttons representing calendar cells
        for (int i = 0; i < CalGrid.size(); i++) {
            final int row = i;
            JButton buttonRow[] = new JButton[50];
            for (int j = 0; j < 50; j++) {
                final int column = j;
                buttonRow[j] = new JButton();

                // IMPORTANT -------------------------------------------------------------------------------------------
                // Because of use of JButtons, our own colors get overridden with system standard
                // When we disable this, we have to manually add lots of tweaks.
                // BETTER CHOICE: Change JButtons to JPanels with MouseListener. Much easier for styling.

                buttonRow[j].setContentAreaFilled(false); // Disable automatic filling
                buttonRow[j].setOpaque(true); // Making sure our colors get selected
                // Creating line to seperate borders
                buttonRow[j].setBorder(BorderFactory.createLineBorder(MolvenoColor.BEIGE.getColor(), 1));

                // Setting a fixed size for all buttons
                buttonRow[j].setPreferredSize(new Dimension(20, 20));
                buttonRow[j].setMinimumSize(new Dimension(20, 20));
                buttonRow[j].setMaximumSize(new Dimension(20, 20));
                // TODO determine button size here based on final app
                buttonRow[j].setMargin(new Insets(8, 8, 8, 8));
                buttonRow[j].setBackground(CalGrid.get(BoatIDs[row])[column].getAvailable() ? MolvenoColor.LIGHT.getColor() : MolvenoColor.DARK.getColor());
                buttonRow[j].addActionListener(e -> {
                    selectTimeCell(row, column);
                });
            }
            allCells[i] = buttonRow;
        }

        JPanel fullCalendar = new JPanel();
        BoxLayout CalBoxLayout = new BoxLayout(fullCalendar, BoxLayout.X_AXIS);
        fullCalendar.setLayout(CalBoxLayout);

        // Here we are creating a separate JPanel for the column of BoatIds
        // Will be removing the if (i==0) block down below
        JPanel idColumn = new JPanel();
        idColumn.setLayout(new BoxLayout(idColumn, BoxLayout.Y_AXIS));
        idColumn.setBorder(new LineBorder(MolvenoColor.DARK.getColor()));
        // Setting the fixed size of this column
        idColumn.setMinimumSize(new Dimension(80, 0));
        idColumn.setPreferredSize(new Dimension(80, 600));

        // Adding a Header to the top of the column
        JLabel header = new  JLabel("Boot ID's:", SwingConstants.CENTER);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        idColumn.add(header);

        for (int i = 0; i < allCells.length; i++) {
            JLabel idLabel = new JLabel("ID: " + BoatIDs[i]);
            idLabel.setHorizontalAlignment(JLabel.CENTER);
            idLabel.setPreferredSize(new Dimension(80, 20));
            idLabel.setMaximumSize(new Dimension(80, 20));
            idLabel.setMinimumSize(new Dimension(80, 20));
            idColumn.add(idLabel);
        }
        fullCalendar.add(idColumn);

        //here the panel containing the calendar is split into 13 pieces; 1 for each hour
        for (int i = 0; i < 13; i++) {
            JPanel CalendarSegment = new JPanel();
            BoxLayout CalSegBox = new BoxLayout(CalendarSegment, BoxLayout.Y_AXIS);
            CalendarSegment.setLayout(CalSegBox);
            CalendarSegment.setBorder(new LineBorder(MolvenoColor.DARK.getColor()));
            int hour = i + 9;
            JLabel headerLabel = new JLabel(hour + ":00");
            CalendarSegment.add(headerLabel);
            //this loop creates a row of cells for each hour, placing cells that were created earlier as part of one 2D grid
            for (int j = 0; j < allCells.length; j++) {
                JPanel cellRow = new JPanel();
                BoxLayout boxLayoutManagerRow = new BoxLayout(cellRow, BoxLayout.X_AXIS);
                cellRow.setLayout(boxLayoutManagerRow);
                // COMMENTED THIS OUT: replaced with idColumn logic above
//                if (i == 0) {
//                    cellRow.add(new JLabel(String.valueOf(BoatIDs[j]))); //TODO make this name of boat probably
//                }
                int zeroPoint = 4 * i;
                int numPanels = 4;
                if (i == 12) {
                    numPanels = 2;
                }
                //this part of the code adds a cell from the "allCells" grid inside of the JPanel
                for (int k = zeroPoint; k < zeroPoint + numPanels; k++) {
                    cellRow.add(allCells[j][k]);
                }
                CalendarSegment.add(cellRow);
            }
            fullCalendar.add(CalendarSegment);
        }
        add(fullCalendar);

        // Here we make a JScrollPane to wrap the fullCalendar in
        JScrollPane scrollPane = new JScrollPane(fullCalendar);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1000, 600));

        add(scrollPane);

        //this panel contains the "reserve" and "cancel" buttons
        JPanel buttonrow = new JPanel();
        BoxLayout boxLayoutManagerRow = new BoxLayout(buttonrow, BoxLayout.X_AXIS);
        buttonrow.setLayout(boxLayoutManagerRow);

        JButton selectButton = new JButton("Reserve");
        selectButton.addActionListener(e -> {
            if (this.selectedStart[0] != null) {
                datePicker.setTextFieldToValidStateIfNeeded();
                dateSelected = datePicker.getDate();
                //start & end times are pulled from the timePoints list, based on the position of the calendar cell in the grid
                String vanTijd = timePoints[this.selectedStart[1]];
                int end;
                if(this.selectedEnd[0] == null){
                    end = this.selectedStart[1];
                }else{
                    end = this.selectedEnd[1];
                }
                //end time is 1 time point after the cell selected as the end point
                String totTijd = timePoints[end + 1];
                //find list of boats that are already reserved in this range of time
                ArrayList<Integer> reservedBoats = new ArrayList<Integer>();
                for(int row = 0; row < allCells.length; row++){
                    for(int col = this.selectedStart[1]; col < end + 1; col++){
                        if(allCells[row][col].getBackground() == MolvenoColor.DARK.getColor()){
                            reservedBoats.add(BoatIDs[row]);
                            break;
                        }
                    }
                }
                ReserveringPopup reserveringPopup = new ReserveringPopup(reservationService, this, vanTijd, totTijd, dateSelected, reservedBoats);
                reserveringPopup.pack();
                reserveringPopup.setLocationRelativeTo(null);
                reserveringPopup.setVisible(true);
            }

        });
        buttonrow.add(selectButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            cancelSelection();
        });
        buttonrow.add(cancelButton);
        add(buttonrow);
    }

    //this function is tied to the cancel button and simply undoes any selections that have already been made
    private void cancelSelection() {
        if(this.selectedStart[0] != null){
            allCells[this.selectedStart[0]][this.selectedStart[1]].setBackground(MolvenoColor.LIGHT.getColor());
            this.selectedStart = new Integer[]{null, null};
            if(this.selectedEnd[0] != null){
                allCells[this.selectedEnd[0]][this.selectedEnd[1]].setBackground(MolvenoColor.LIGHT.getColor());
                this.selectedEnd = new Integer[]{null, null};
            }
        }
    }

    public void reserveBoat(ArrayList<Integer> selectedBoats) {
        //first, establish an end time based on whether or not more than 1 cell is selected
        int endTime;
        if(this.selectedEnd[0] == null){
            endTime = this.selectedStart[1];
        }else{
            endTime = this.selectedEnd[1];
        }
        //for every selected boat, make cells in time selection black
        for(int i = 0; i < selectedBoats.size(); i++){
            int row = Arrays.asList(BoatIDs).indexOf(selectedBoats.get(i));

            for(int j = this.selectedStart[1]; j < endTime + 1; j++){
                this.Calendar.getCalendarGrid().get(BoatIDs[row])[j].reserve();
                allCells[row][j].setBackground(MolvenoColor.DARK.getColor());
            }
        }
        //make green & red cells white again
        if(allCells[this.selectedStart[0]][this.selectedStart[1]].getBackground() != MolvenoColor.DARK.getColor()){
            allCells[this.selectedStart[0]][this.selectedStart[1]].setBackground(MolvenoColor.LIGHT.getColor());
        }
        if(this.selectedEnd[0] != null && allCells[this.selectedEnd[0]][this.selectedEnd[1]].getBackground() != MolvenoColor.DARK.getColor()){
            allCells[this.selectedEnd[0]][this.selectedEnd[1]].setBackground(MolvenoColor.LIGHT.getColor());
        }
        //reset markers for selected cells
        this.selectedStart = new Integer[]{null, null};
        this.selectedEnd = new Integer[]{null, null};
    }


    /* this is the function that handles the selection of cells by clicking on them.
    selectedStart & selectedEnd keep track of which cells are selected by row & column.
    background is set to green when set as the starting cell, to red when set as ending cell
    previously selected cells are set to white when a new selection is made
    black cells are already selected
    */
    private void selectTimeCell(int row, int column) {
        //scenario 1: no selection has been made yet
        if(this.selectedStart[0] == null && allCells[row][column].getBackground() != MolvenoColor.DARK.getColor()){
            if(allCells[row][column].getBackground() != MolvenoColor.DARK.getColor()){
                allCells[row][column].setBackground(MolvenoColor.GREEN.getColor());
                this.selectedStart = new Integer[]{row, column};
            }
        //scenario 2: new boat selected
        }else if(this.selectedStart[0] != row && allCells[row][column].getBackground() != MolvenoColor.DARK.getColor()){
            allCells[row][column].setBackground(MolvenoColor.GREEN.getColor());
            allCells[this.selectedStart[0]][this.selectedStart[1]].setBackground(MolvenoColor.LIGHT.getColor());
            this.selectedStart = new Integer[]{row, column};
            if (this.selectedEnd[0] != null){
                allCells[this.selectedEnd[0]][this.selectedEnd[1]].setBackground(MolvenoColor.LIGHT.getColor());
                this.selectedEnd = new Integer[]{null, null};
            }
        }else{
            //scenario 3: select current end point to turn it into the start point
            if(this.selectedEnd[1] != null && this.selectedEnd[1] == column){
                allCells[this.selectedStart[0]][this.selectedStart[1]].setBackground(MolvenoColor.LIGHT.getColor());
                allCells[row][column].setBackground(MolvenoColor.GREEN.getColor());
                this.selectedStart = new Integer[]{row, column};
                this.selectedEnd = new Integer[]{null, null};
            }else if(column > this.selectedStart[1]){
                //check if the block attempting to be reserved is available
                boolean allAvailable = true;
                for(int i = this.selectedStart[1]; i < column + 1; i++){
                    if(allCells[row][i].getBackground() == MolvenoColor.DARK.getColor()){
                        allAvailable = false;
                        break;
                    }
                }
                //scenario 4: new endpoint selected
                if(allAvailable){
                    allCells[row][column].setBackground(MolvenoColor.ORANGE.getColor());
                    if (this.selectedEnd[0] != null){
                        allCells[this.selectedEnd[0]][this.selectedEnd[1]].setBackground(MolvenoColor.LIGHT.getColor());
                    }
                    this.selectedEnd = new Integer[]{row, column};
                //scenario 5: new start point selected past reserved block
                }else if(allCells[row][column].getBackground() != MolvenoColor.DARK.getColor()){
                    allCells[this.selectedStart[0]][this.selectedStart[1]].setBackground(MolvenoColor.LIGHT.getColor());
                    allCells[row][column].setBackground(MolvenoColor.GREEN.getColor());
                    this.selectedStart = new Integer[]{row, column};
                    if (this.selectedEnd[0] != null){
                        allCells[this.selectedEnd[0]][this.selectedEnd[1]].setBackground(MolvenoColor.LIGHT.getColor());
                        this.selectedEnd = new Integer[]{null, null};
                    }
                }
            }else {
                //scenario 6: new start point selected for the same boat (same as scenario 4)
                if(allCells[row][column].getBackground() != MolvenoColor.DARK.getColor()){
                    allCells[this.selectedStart[0]][this.selectedStart[1]].setBackground(MolvenoColor.LIGHT.getColor());
                    allCells[row][column].setBackground(MolvenoColor.GREEN.getColor());
                    this.selectedStart = new Integer[]{row, column};
                    if (this.selectedEnd[0] != null){
                        allCells[this.selectedEnd[0]][this.selectedEnd[1]].setBackground(MolvenoColor.LIGHT.getColor());
                        this.selectedEnd = new Integer[]{null, null};
                    }
                }
            }

        }
    }

    public Calendar getCalendar(){return this.Calendar;}
}
