package presentatie;

import domain.MolvenoColor;
import domain.MolvenoFont;
import domain.ReservationRecord;
import service.ReservationService.ReservationService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ReservationsTab extends JPanel {
    private JPanel mainPanel;
    private JTable table1;
    private JPanel cardLayout;
    private JTextField searchField;
    private JPanel searchPanel;
    private JPanel tablePanel;
    private JLabel searchLabel;
    private JPanel gapPanel;
    private DefaultTableModel tableModel;
    private ReservationService reservationService;

    public ReservationsTab(ReservationService reservationService) {
        this.reservationService = reservationService;
        this.setLayout(new java.awt.BorderLayout());
        mainPanel.setBackground(MolvenoColor.LIGHT.getColor());
        mainPanel.setOpaque(true);
        add(mainPanel, java.awt.BorderLayout.CENTER);

        gapPanel.setBackground(MolvenoColor.LIGHT.getColor());
        gapPanel.setOpaque(true);

        String[] columnNames = {"<html><b>Reserveringsnummer<b><html>",
                "<html><b>Naam<b><html>",
                "<html><b>Telefoonnummer<b><html>",
                "<html><b>E-mailadres<b><html>",
                "<html><b>Datum<b><html>",
                "<html><b>Begintijd<b><html>",
                "<html><b>Eindtijd<b><html>"
        };

        String[][] tableData = {};

        tableModel = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        table1.setModel(tableModel);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // table1.setBounds(30, 40, 900, 400);

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
                    if (viewRow != -1) {
                        int modelRow = table1.convertRowIndexToModel(viewRow);
                        int reservationNumber = (Integer) tableModel.getValueAt(modelRow, 0);

                        var reserveringPopup = new ReserveringPopup(reservationService,
                                reservationService.getReservationWithReservationNumber(reservationNumber)) {
                            @Override
                            public void dispose() {
                                super.dispose();
                                reloadTable();
                            }
                        };
                        reserveringPopup.pack();
                        reserveringPopup.setLocationRelativeTo(null);
                        reserveringPopup.setVisible(true);
                    }
                }
            }
        });

        searchStyling();
        tableStyling();
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
                String phoneNumber;
                if(reservationRecord.customerRecord().telefoonNummer() != null){
                    phoneNumber = Integer.toString(reservationRecord.customerRecord().telefoonNummer());
                }else{
                    phoneNumber = "";
                }
                Object[] reservationFields = {
                        reservationRecord.reserveringsNummer(),
                        reservationRecord.customerRecord().naam(),
                        phoneNumber,
                        reservationRecord.customerRecord().email(),
                        reservationRecord.datum(),
                        reservationRecord.begintijd(),
                        reservationRecord.eindtijd()
                        };
                tableModel.addRow(reservationFields);
            }
        }
    }

    public void tableStyling() {
        TableColumn columnReserveringsNummer = table1.getColumnModel().getColumn(0);
        TableColumn columnEmail = table1.getColumnModel().getColumn(3);
        TableColumn columnBegintijd = table1.getColumnModel().getColumn(5);
        TableColumn columnKamerEindtijd = table1.getColumnModel().getColumn(6);
        columnReserveringsNummer.setPreferredWidth(70);
        columnEmail.setPreferredWidth(130);
        columnBegintijd.setPreferredWidth(25);
        columnKamerEindtijd.setPreferredWidth(25);

        int margin = 10;
        table1.setRowHeight(MolvenoFont.PARAGRAPH.getFont().getSize() + margin);

        javax.swing.table.JTableHeader header = table1.getTableHeader();
        header.setPreferredSize(new  Dimension(header.getPreferredSize().width, MolvenoFont.PARAGRAPH.getFont().getSize() + margin*2));
        header.setOpaque(false);

        if (table1.getParent() instanceof JViewport) {
            table1.getParent().setBackground(MolvenoColor.LIGHT.getColor());
        }
        table1.setFont(MolvenoFont.PARAGRAPH.getFont());
        table1.setForeground(MolvenoColor.DARK.getColor());
        table1.setBackground(MolvenoColor.LIGHT.getColor());

        table1.setSelectionBackground(MolvenoColor.BLUE.getColor());
        table1.setSelectionForeground(MolvenoColor.DARK.getColor());
        table1.setFocusable(false);
        table1.setShowGrid(true);
        table1.setGridColor(MolvenoColor.BEIGE.getColor());

        DefaultTableCellRenderer marginRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                setFont(MolvenoFont.PARAGRAPH.getFont());
                if (isSelected) {
                    setBackground(table.getSelectionBackground());
                    setForeground(table.getSelectionForeground());
                } else {
                    setBackground(MolvenoColor.LIGHT.getColor());
                    setForeground(MolvenoColor.DARK.getColor());
                }

                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, MolvenoColor.BEIGE.getColor()),
                        BorderFactory.createEmptyBorder(0, 5, 0, 0)
                ));
                return this;
            }
        };

        for (int i = 0; i < table1.getColumnCount(); i++) {
            table1.getColumnModel().getColumn(i).setCellRenderer(marginRenderer);
        }

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                setBackground(MolvenoColor.BLUE.getColor());
                setForeground(MolvenoColor.DARK.getColor());
                setFont(MolvenoFont.BUTTONLABEL.getFont());

                setHorizontalAlignment(JLabel.LEFT);

                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 2, MolvenoColor.BEIGE.getColor()),
                        BorderFactory.createEmptyBorder(0, 5, 0, 0)
                ));
                return this;
            }
        });
    }

    public void searchStyling() {
        searchPanel.setBackground(MolvenoColor.BLUE.getColor());
        searchPanel.setPreferredSize(new Dimension(searchPanel.getPreferredSize().width, 60));

        searchLabel.setText("<html><b>Zoeken: <b><html>");
        searchLabel.setFont(MolvenoFont.PARAGRAPH.getFont());
        searchLabel.setBackground(MolvenoColor.LIGHT.getColor());
        searchLabel.setForeground(MolvenoColor.DARK.getColor());

        searchField.setFont(MolvenoFont.PARAGRAPH.getFont());
        searchField.setBackground(MolvenoColor.LIGHT.getColor());
        searchField.setForeground(MolvenoColor.DARK.getColor());
    }
}