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
        this.allCells = new JButton[20][20];
//        JPanel headerRow = new JPanel(); //TODO maybe seperate everything in JPanels per hour. code relies on buttons being one grid tho
//        BoxLayout headerLayout = new BoxLayout(headerRow, BoxLayout.X_AXIS);
//        headerRow.setLayout(headerLayout);
//        JLabel headerLabels[] = new JLabel[13];
//        for(int i = 9; i < 21; i++){
//            int labelnum = i-9;
//            headerLabels[labelnum] = new JLabel(i + ":00");
//            headerLabels[labelnum].setBorder(new EmptyBorder(new Insets(8, 0, 8, 64)));
//            headerLabels[labelnum].setSize(new Dimension(32, 1));
//            headerLabels[labelnum].setBackground(Color.CYAN);
//            headerRow.add(headerLabels[labelnum]);
//        }
//        headerLabels[12] = new JLabel("21:00");
//        headerLabels[12].setBorder(new EmptyBorder(new Insets(8, 0, 8, 32)));
//        headerLabels[12].setSize(new Dimension(16, 1));
//        headerLabels[12].setBackground(Color.CYAN);
//        headerRow.add(headerLabels[12]);

 //       DisplayPanel.add(headerRow);

        HashMap<String, CalendarCell[]>  CalGrid = Calendar.getCalendarGrid();
        for(int i = 0; i < CalGrid.size(); i++){
            final int row = i;
            JPanel cellRow = new JPanel();
            BoxLayout boxLayoutManagerRow = new BoxLayout(cellRow, BoxLayout.X_AXIS);
            cellRow.setLayout(boxLayoutManagerRow);
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
                cellRow.add(buttonRow[j]);
            }
            allCells[i] = buttonRow;
            DisplayPanel.add(cellRow);
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
