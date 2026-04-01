import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;

public class CalendarDisplay {

    private Calendar Calendar;
    private JPanel DisplayPanel = new JPanel();
    private String[] BoatIDs = {"Boat1", "Boat2", "Boat3"}; //TODO placeholder data; maybe let user input selection of them
    private JButton[][] allCells;
    private Integer[] selectedStart = {null, null};
    private Integer[] selectedEnd = {null, null};

    public CalendarDisplay(Calendar Calendar){
        this.Calendar = Calendar;
        JFrame frame = new JFrame("Set Reservation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BoxLayout boxLayoutManager = new BoxLayout(DisplayPanel, BoxLayout.Y_AXIS);
        DisplayPanel.setLayout(boxLayoutManager);
        this.allCells = new JButton[BoatIDs.length][50];


        HashMap<String, CalendarCell[]>  CalGrid = Calendar.getCalendarGrid();
        for(int i = 0; i < CalGrid.size(); i++){
            final int row = i;
            JButton buttonRow[] = new JButton[50];
            for(int j = 0; j < 50; j++){
                final int column = j;
                buttonRow[j] = new JButton();
                //TODO determine button size here based on final app
                buttonRow[j].setMargin(new Insets(8, 8, 8, 8));
                buttonRow[j].setBackground(CalGrid.get(BoatIDs[row])[column].getAvailable() ? Color.WHITE: Color.BLACK);
                buttonRow[j].addActionListener(e -> {
                    selectTimeCell(row, column);
                });
            }
            allCells[i] = buttonRow;
        }

        JPanel fullCalendar = new JPanel();
        BoxLayout CalBoxLayout = new BoxLayout(fullCalendar, BoxLayout.X_AXIS);
        fullCalendar.setLayout(CalBoxLayout);
        for(int i = 0; i < 13; i++){
            JPanel CalendarSegment = new JPanel();
            BoxLayout CalSegBox = new BoxLayout(CalendarSegment, BoxLayout.Y_AXIS);
            CalendarSegment.setLayout(CalSegBox);
            CalendarSegment.setBorder(new LineBorder(Color.BLACK));
            int hour = i+9;
            JLabel headerLabel = new JLabel(hour + ":00");
            CalendarSegment.add(headerLabel);
            for(int j = 0; j < allCells.length; j++){
                JPanel cellRow = new JPanel();
                BoxLayout boxLayoutManagerRow = new BoxLayout(cellRow, BoxLayout.X_AXIS);
                cellRow.setLayout(boxLayoutManagerRow);
                if(i == 0){
                    cellRow.add(new JLabel(BoatIDs[j]));
                }
                int zeroPoint = 4 * i;
                int numPanels = 4;
                if(i == 12){
                    numPanels = 2;
                }
                for(int k = zeroPoint; k < zeroPoint + numPanels; k++){
                    cellRow.add(allCells[j][k]);
                }
                CalendarSegment.add(cellRow);
            }
            fullCalendar.add(CalendarSegment);
        }
        DisplayPanel.add(fullCalendar);

        JPanel buttonrow = new JPanel();
        BoxLayout boxLayoutManagerRow = new BoxLayout(buttonrow, BoxLayout.X_AXIS);
        buttonrow.setLayout(boxLayoutManagerRow);

        JButton selectButton = new JButton("Reserve");
        selectButton.addActionListener(e -> {
            reserveBoat();
        });
        buttonrow.add(selectButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            cancelSelection();
        });
        buttonrow.add(cancelButton);
        DisplayPanel.add(buttonrow);
        frame.add(this.DisplayPanel);


        frame.setSize(1280, 720);
        frame.setVisible(true);
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

    private void reserveBoat() {
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

    public void show(){
        System.out.println("show");
    }
}
