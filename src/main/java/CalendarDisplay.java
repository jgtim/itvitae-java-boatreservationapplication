import javax.swing.*;
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
        this.allCells = new JButton[20][20];

        HashMap<String, CalendarCell[]>  CalGrid = Calendar.getCalendarGrid();
        for(int i = 0; i < CalGrid.size(); i++){
            final int row = i;
            JPanel panelrow = new JPanel();
            BoxLayout boxLayoutManagerRow = new BoxLayout(panelrow, BoxLayout.X_AXIS);
            panelrow.setLayout(boxLayoutManagerRow);
            JButton buttonRow[] = new JButton[CalGrid.get(BoatIDs[i]).length];
            for(int j = 0; j < CalGrid.get(BoatIDs[i]).length; j++){
                final int column = j;
                buttonRow[j] = new JButton();
                //TODO determine button size here based on final app
                buttonRow[j].setMargin(new Insets(5, 5, 5, 5));
                //buttonRow[j].setSize(new Dimension(50, 50));
                buttonRow[j].setBackground(CalGrid.get(BoatIDs[row])[column].getAvailable() ? Color.WHITE: Color.BLACK);
                buttonRow[j].addActionListener(e -> {
                    selectTimeCell(row, column);
                });
                panelrow.add(buttonRow[j]);
            }
            allCells[i] = buttonRow;
            DisplayPanel.add(panelrow);
        }

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


        frame.setSize(1000, 200);
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
