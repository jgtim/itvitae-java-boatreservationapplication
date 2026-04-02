package presentatie.Calendar;

import com.github.lgooddatepicker.components.DatePicker;
import presentatie.ReserveringPopup;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;

public class CalendarDisplay extends JPanel {
    private Calendar Calendar;
    private JPanel DisplayPanel = new JPanel();
    private String[] BoatIDs = {"Boat1", "Boat2", "Boat3"}; //TODO placeholder data; maybe let user input selection of them
    private JButton[][] allCells;
    private Integer[] selectedStart = {null, null};
    private Integer[] selectedEnd = {null, null};
    private String[] timePoints = {"9:00", "9:15", "9:30", "9:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15",
            "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15",
            "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15",
            "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15",
            "20:30", "20:45", "21:00", "21:15", "21:30"};

    public CalendarDisplay() {

        this.Calendar = new Calendar();

        BoxLayout boxLayoutManager = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayoutManager);

        var datePicker = new DatePicker();
        datePicker.setMaximumSize(new Dimension(200, 25));
        datePicker.setDateToToday();
        add(datePicker);

        this.allCells = new JButton[BoatIDs.length][50];

        HashMap<String, CalendarCell[]> CalGrid = Calendar.getCalendarGrid();
        for (int i = 0; i < CalGrid.size(); i++) {
            final int row = i;
            JButton buttonRow[] = new JButton[50];
            for (int j = 0; j < 50; j++) {
                final int column = j;
                buttonRow[j] = new JButton();
                //TODO determine button size here based on final app
                buttonRow[j].setMargin(new Insets(8, 8, 8, 8));
                buttonRow[j].setBackground(CalGrid.get(BoatIDs[row])[column].getAvailable() ? Color.WHITE : Color.BLACK);
                buttonRow[j].addActionListener(e -> {
                    selectTimeCell(row, column);
                });
            }
            allCells[i] = buttonRow;
        }

        JPanel fullCalendar = new JPanel();
        BoxLayout CalBoxLayout = new BoxLayout(fullCalendar, BoxLayout.X_AXIS);
        fullCalendar.setLayout(CalBoxLayout);
        for (int i = 0; i < 13; i++) {
            JPanel CalendarSegment = new JPanel();
            BoxLayout CalSegBox = new BoxLayout(CalendarSegment, BoxLayout.Y_AXIS);
            CalendarSegment.setLayout(CalSegBox);
            CalendarSegment.setBorder(new LineBorder(Color.BLACK));
            int hour = i + 9;
            JLabel headerLabel = new JLabel(hour + ":00");
            CalendarSegment.add(headerLabel);
            for (int j = 0; j < allCells.length; j++) {
                JPanel cellRow = new JPanel();
                BoxLayout boxLayoutManagerRow = new BoxLayout(cellRow, BoxLayout.X_AXIS);
                cellRow.setLayout(boxLayoutManagerRow);
                if (i == 0) {
                    cellRow.add(new JLabel(BoatIDs[j]));
                }
                int zeroPoint = 4 * i;
                int numPanels = 4;
                if (i == 12) {
                    numPanels = 2;
                }
                for (int k = zeroPoint; k < zeroPoint + numPanels; k++) {
                    cellRow.add(allCells[j][k]);
                }
                CalendarSegment.add(cellRow);
            }
            fullCalendar.add(CalendarSegment);
        }
        add(fullCalendar);

        JPanel buttonrow = new JPanel();
        BoxLayout boxLayoutManagerRow = new BoxLayout(buttonrow, BoxLayout.X_AXIS);
        buttonrow.setLayout(boxLayoutManagerRow);

        JButton selectButton = new JButton("Reserve");
        selectButton.addActionListener(e -> {
            if (this.selectedStart[0] != null) {
                //reserveBoat();
                String vanTijd = timePoints[this.selectedStart[1]];
                int end;
                if(this.selectedEnd[0] == null){
                    end = this.selectedStart[1];
                }else{
                    end = this.selectedEnd[1];
                }
                String totTijd = timePoints[end + 1];
                String date = datePicker.getDate().toString();

                ReserveringPopup reserveringPopup = new ReserveringPopup(this, vanTijd, totTijd, date);
                //ReserveringPopup reserveringPopup = new ReserveringPopup(datePicker.getDate());
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

    private void cancelSelection() {
        if(this.selectedStart[0] != null){
            allCells[this.selectedStart[0]][this.selectedStart[1]].setBackground(Color.WHITE);
            this.selectedStart = new Integer[]{null, null};
            if(this.selectedEnd[0] != null){
                allCells[this.selectedEnd[0]][this.selectedEnd[1]].setBackground(Color.WHITE);
                this.selectedEnd = new Integer[]{null, null};
            }
        }
    }

    public void reserveBoat() {
        //TODO this should happen after doing stuff in the popup window
        if(this.selectedStart[0] != null){
            int endTime;
            if(this.selectedEnd[0] == null){
                endTime = this.selectedStart[1];
            }else{
                endTime = this.selectedEnd[1];
            }
            for(int i = this.selectedStart[1]; i < endTime + 1; i++){
                this.Calendar.getCalendarGrid().get(BoatIDs[this.selectedStart[0]])[i].reserve();
                allCells[this.selectedStart[0]][i].setBackground(Color.BLACK);
            }
            this.selectedStart = new Integer[]{null, null};
            this.selectedEnd = new Integer[]{null, null};
        }
    }

    private void selectTimeCell(int row, int column) {
        //scenario 1: no selection has been made yet
        if(this.selectedStart[0] == null && allCells[row][column].getBackground() != Color.BLACK){
            allCells[row][column].setBackground(Color.GREEN);
            this.selectedStart = new Integer[]{row, column};
        //scenario 2: new boat selected
        }else if(this.selectedStart[0] != row && allCells[row][column].getBackground() != Color.BLACK){
            allCells[row][column].setBackground(Color.GREEN);
            allCells[this.selectedStart[0]][this.selectedStart[1]].setBackground(Color.WHITE);
            this.selectedStart = new Integer[]{row, column};
            if (this.selectedEnd[0] != null){
                allCells[this.selectedEnd[0]][this.selectedEnd[1]].setBackground(Color.WHITE);
                this.selectedEnd = new Integer[]{null, null};
            }
        }else{
            //scenario 3: select current end point to turn it into the start point
            if(this.selectedEnd[1] != null && this.selectedEnd[1] == column){
                allCells[this.selectedStart[0]][this.selectedStart[1]].setBackground(Color.WHITE);
                allCells[row][column].setBackground(Color.GREEN);
                this.selectedStart = new Integer[]{row, column};
                this.selectedEnd = new Integer[]{null, null};
            }else if(column > this.selectedStart[1]){
                //check if the block attempting to be reserved is available
                boolean allAvailable = true;
                for(int i = this.selectedStart[1]; i < column + 1; i++){
                    if(allCells[row][i].getBackground() == Color.BLACK){
                        allAvailable = false;
                        break;
                    }
                }
                //scenario 4: new endpoint selected
                if(allAvailable){
                    allCells[row][column].setBackground(Color.RED);
                    if (this.selectedEnd[0] != null){
                        allCells[this.selectedEnd[0]][this.selectedEnd[1]].setBackground(Color.WHITE);
                    }
                    this.selectedEnd = new Integer[]{row, column};
                //scenario 5: new start point selected past reserved block
                }else if(allCells[row][column].getBackground() != Color.BLACK){
                    allCells[this.selectedStart[0]][this.selectedStart[1]].setBackground(Color.WHITE);
                    allCells[row][column].setBackground(Color.GREEN);
                    this.selectedStart = new Integer[]{row, column};
                    if (this.selectedEnd[0] != null){
                        allCells[this.selectedEnd[0]][this.selectedEnd[1]].setBackground(Color.WHITE);
                        this.selectedEnd = new Integer[]{null, null};
                    }
                }
            }else {
                //scenario 6: new start point selected for the same boat (same as scenario 4)
                if(allCells[row][column].getBackground() != Color.BLACK){
                    allCells[this.selectedStart[0]][this.selectedStart[1]].setBackground(Color.WHITE);
                    allCells[row][column].setBackground(Color.GREEN);
                    this.selectedStart = new Integer[]{row, column};
                    if (this.selectedEnd[0] != null){
                        allCells[this.selectedEnd[0]][this.selectedEnd[1]].setBackground(Color.WHITE);
                        this.selectedEnd = new Integer[]{null, null};
                    }
                }
            }

        }
    }

    public Calendar getCalendar(){return this.Calendar;}
}
