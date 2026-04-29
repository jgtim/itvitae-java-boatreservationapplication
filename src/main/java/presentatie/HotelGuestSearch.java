package presentatie;

import data.BoatDataExcel;
import data.CustomerDataExcel;
import domain.HotelGuestRecord;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class HotelGuestSearch extends JDialog {
    private JTextField searchInputField;
    private JButton searchButton;
    private JTable ResultsTable;
    private JPanel contentPane;
    private ArrayList<HotelGuestRecord> allGuests;

    public HotelGuestSearch(){
        var guestDataHandler = new CustomerDataExcel();
        allGuests = guestDataHandler.read();
        setContentPane(contentPane);
        setModal(true);
        searchButton.addActionListener(e -> onSearch());
        //TODO add double click for table cell
        ResultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = ResultsTable.rowAtPoint(e.getPoint());
                    //TODO can I add guest record to row? maybe make those a seperate array

                }
            }
        });
    }

    private void onSearch() {


    }
}
