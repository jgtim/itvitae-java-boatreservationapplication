package presentatie;

import data.ReservationCsvHandler;
import data.ReservationRepositoryHandler;
import service.ReservationDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class ReservationsTab extends JPanel {
    private JPanel mainPanel;
    private JTable table1;
    private JPanel card2;
    private ReservationItemPanel reservationItemPanel;
    private JPanel cardLayout;
    private DefaultTableModel tableModel;
    private ReservationRepositoryHandler reservationRepositoryHandler;

    public ReservationsTab(ReservationRepositoryHandler repoHandler) {
        reservationItemPanel.setCardLayoutParent(cardLayout);
        reservationRepositoryHandler = repoHandler;
        add(mainPanel);

        String[] columnNames = {"Naam", "Telefoonnummer", "Email", "Betaald", "Kamerfacturatie", "Datum", "Begintijd", "Eindtijd", "Boat IDs"};

        String[][] tableData = {};

        tableModel = new DefaultTableModel(tableData, columnNames);
        table1.setModel(tableModel);
        table1.setBounds(30, 40, 900, 400);

        TableColumn columnBetaald = table1.getColumnModel().getColumn(3);
        TableColumn columnKamerFacturatie = table1.getColumnModel().getColumn(4);
        columnBetaald.setPreferredWidth(50);
        columnKamerFacturatie.setPreferredWidth(50);

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
                String[] reservationFields = reservationDto.toString().split(", ");
                reservationFields[reservationFields.length-1] = reservationFields[reservationFields.length-1].replace(".", ",");
                tableModel.addRow(reservationFields);
            }
        }
    }
}