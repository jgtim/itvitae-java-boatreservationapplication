package presentatie;

import data.BoatDataExcel;
import data.CustomerDataExcel;

import javax.swing.*;

public class HotelGuestSearch extends JDialog {
    private JTextField searchInputField;
    private JButton searchButton;
    private JTable ResultsTable;

    public HotelGuestSearch(){
        var guestDataHandler = new CustomerDataExcel();

    }
}
