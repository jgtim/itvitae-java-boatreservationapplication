package presentatie;

import data.ReservationCsvHandler;
import service.ReservationDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class ReservationsTab extends JPanel {
    private JPanel mainPanel;
    private JTable table1;

    public ReservationsTab() {
        add(mainPanel);

        String[] columnNames = {"Naam", "Telefoonnummer", "Email", "Betaald", "Kamerfacturatie"};

        String[][] tableData = {{"John Doe", "1234 5678", "johndoe@example.com", "true", "false"}};

        DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames);
        table1.setModel(tableModel);
        table1.setBounds(30, 40, 200, 300);

        var reservationCsvHandler = new ReservationCsvHandler();
        ArrayList<ReservationDto> reservationDtos = reservationCsvHandler.getReservationList();

        for (ReservationDto reservationDto : reservationDtos) {
            tableModel.addRow(reservationDto.toString().split(", "));
        }
    }
}