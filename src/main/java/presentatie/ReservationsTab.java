package presentatie;

import data.ReservationCsvHandler;
import data.ReservationRepositoryHandler;
import service.ReservationDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class ReservationsTab extends JPanel {
    private JPanel mainPanel;
    private JTable table1;
    private DefaultTableModel tableModel;
    private ReservationRepositoryHandler reservationRepositoryHandler;

    public ReservationsTab(ReservationRepositoryHandler repoHandler) {
        reservationRepositoryHandler = repoHandler;

        add(mainPanel);

        String[] columnNames = {"Naam", "Telefoonnummer", "Email", "Betaald", "Kamerfacturatie"};

        String[][] tableData = {};

        tableModel = new DefaultTableModel(tableData, columnNames);
        table1.setModel(tableModel);
        table1.setBounds(30, 40, 200, 300);

        reloadTable();
    }

    public void onSelected() {
        reloadTable();
    }

    private void reloadTable() {
        tableModel.setRowCount(0);
        ArrayList<ReservationDto> reservationDtos = reservationRepositoryHandler.getReservationList();

        if (reservationDtos != null) {
            for (ReservationDto reservationDto : reservationDtos) {
                tableModel.addRow(reservationDto.toString().split(", "));
            }
        }
    }
}