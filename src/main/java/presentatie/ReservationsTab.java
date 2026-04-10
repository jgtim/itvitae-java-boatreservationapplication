package presentatie;

import domain.ReservationRecord;
import service.ReservationService.ReservationService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ReservationsTab extends JPanel {
    private JPanel mainPanel;
    private JTable table1;
    private JPanel card2;
    private ReservationItemPanel reservationItemPanel;
    private JPanel cardLayout;
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private ReservationService reservationService;

    public ReservationsTab(ReservationService reservationService) {
        reservationItemPanel.setCardLayoutParent(cardLayout);
        this.reservationService = reservationService;
        add(mainPanel);

        String[] columnNames = {"Rnummer", "Naam", "Telefoonnummer", "Email", "Datum", "Begintijd", "Eindtijd"};

        String[][] tableData = {};

        tableModel = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table1.setModel(tableModel);
        table1.setBounds(30, 40, 900, 400);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel) {
            @Override
            public boolean isSortable(int column) {
                return column == 0 || column == 1;
            }
        };
        table1.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSearch();
            }
            public void updateSearch() {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchField.getText(), 0, 1));
            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int viewRow = table1.rowAtPoint(e.getPoint());
                    int reservationNumber = (Integer) table1.getModel().getValueAt(viewRow, 0);
//                    System.out.println(reservationNumber);
//                    reservationItemPanel.changeReservation(reservationService.getReservationWithReservationNumber(reservationNumber));
//                    CardLayout cl = (CardLayout) cardLayout.getLayout();
//                    cl.next(cardLayout);
                    var reserveringPopup = new ReserveringPopup(reservationService.getReservationWithReservationNumber(reservationNumber));
                    reserveringPopup.pack();
                    reserveringPopup.setLocationRelativeTo(null);
                    reserveringPopup.setVisible(true);
                }
            }
        });



        TableColumn columnReserveringsNummer = table1.getColumnModel().getColumn(0);
        TableColumn columnEmail = table1.getColumnModel().getColumn(3);
        TableColumn columnBegintijd = table1.getColumnModel().getColumn(5);
        TableColumn columnKamerEindtijd = table1.getColumnModel().getColumn(6);
        columnReserveringsNummer.setPreferredWidth(50);
        columnEmail.setPreferredWidth(130);
        columnBegintijd.setPreferredWidth(30);
        columnKamerEindtijd.setPreferredWidth(30);

        reloadTable();
    }

    public void onSelected() {
        reloadTable();
    }

    private void reloadTable() {
        tableModel.setRowCount(0);
        ArrayList<ReservationRecord> reservationRecords = reservationService.getReservations();

        if (reservationRecords != null) {
            for (ReservationRecord reservationRecord : reservationRecords) {
                Object[] reservationFields = {
                        reservationRecord.reserveringsNummer(),
                        reservationRecord.customerRecord().naam(),
                        Integer.toString(reservationRecord.customerRecord().telefoonNummer()),
                        reservationRecord.customerRecord().email(),
                        reservationRecord.datum(),
                        reservationRecord.begintijd(),
                        reservationRecord.eindtijd()
                        };
                tableModel.addRow(reservationFields);
            }
        }
    }
}