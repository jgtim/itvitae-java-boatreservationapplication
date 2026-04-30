package presentatie;

import data.CustomerDataExcel;
import domain.HotelGuestRecord;
import domain.ReservationRecord;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class HotelGuestSearch extends JDialog {
    private JTextField searchInputField;
    private JButton searchButton;
    private JTable ResultsTable;
    private JPanel contentPane;
    private ArrayList<HotelGuestRecord> allGuests;
    private ArrayList<HotelGuestRecord> foundGuests;
    public HotelGuestRecord selectedGuest;
    private DefaultTableModel tableModel;


    public HotelGuestSearch(){
        var guestDataHandler = new CustomerDataExcel();
        allGuests = guestDataHandler.read();
        setContentPane(contentPane);
        setModal(true);
        searchButton.addActionListener(e -> onSearch());
        String[] columnNames = {"Achternaam", "Voornaam", "Kamernummer", "Telefoonnummer", "Email"};

        String[][] tableData = {};

        tableModel = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ResultsTable.setModel(tableModel);
        ResultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = ResultsTable.rowAtPoint(e.getPoint());
                    selectedGuest = foundGuests.get(selectedRow);
                    dispose();

                }
            }
        });
    }

    private void onSearch() {
        String searchTerm = searchInputField.getText().toLowerCase();
        foundGuests = new ArrayList<>();
        if(StringUtils.isNumeric(searchTerm)){
            int roomNumber = Integer.parseInt(searchTerm);
            for(int i = 0; i < allGuests.size(); i++){
                if(allGuests.get(i).kamernummer().equals(roomNumber)){
                    foundGuests.add(allGuests.get(i));
                }
            }
        }else{
            for(int i = 0; i < allGuests.size(); i++){
                if(allGuests.get(i).achternaam().toLowerCase().equals(searchTerm)){
                    foundGuests.add(allGuests.get(i));
                }
            }
        }
        reloadTable();
    }

    private void reloadTable() {
        tableModel.setRowCount(0);
        for (HotelGuestRecord guest : foundGuests) {
            String phoneNumber;
            if(guest.telefoonnummer() != null){
                phoneNumber = Integer.toString(guest.telefoonnummer());
            }else{
                phoneNumber = "";
            }
            Object[] guestFields = {
                    guest.achternaam(),
                    guest.voornaam(),
                    guest.kamernummer(),
                    phoneNumber,
                    guest.email()
            };
            tableModel.addRow(guestFields);
        }
    }
}
