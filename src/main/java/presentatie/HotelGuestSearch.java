package presentatie;

import data.CustomerDataExcel;
import domain.HotelGuestRecord;
import domain.MolvenoColor;
import domain.MolvenoFont;
import domain.ReservationRecord;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class HotelGuestSearch extends JDialog {
    private JTextField searchInputField;
    private JButton searchButton;
    private JTable ResultsTable;
    private JPanel contentPane;
    private JLabel searchLabel;
    private JPanel searchPanel;
    private JScrollPane scrollPane;
    private JPanel emptyPanel;
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

        stylingPanel();
        styleTable();
        styleScrollPane();
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

    private void stylingPanel() {
        contentPane.setBackground(MolvenoColor.LIGHT.getColor());
        contentPane.setPreferredSize(new Dimension(850, 400));

        searchPanel.setBackground(MolvenoColor.BLUE.getColor());
        searchPanel.setPreferredSize(new Dimension(850, 50));

        searchLabel.setFont(MolvenoFont.PARAGRAPH.getFont());
        searchLabel.setBackground(MolvenoColor.BLUE.getColor());
        searchLabel.setForeground(MolvenoColor.DARK.getColor());

        searchInputField.setFont(MolvenoFont.PARAGRAPH.getFont());
        searchInputField.setBackground(MolvenoColor.LIGHT.getColor());
        searchInputField.setForeground(MolvenoColor.DARK.getColor());

        searchButton.setFont(MolvenoFont.BUTTONLABEL.getFont());
        searchButton.setBackground(MolvenoColor.BEIGE.getColor());
        searchButton.setForeground(MolvenoColor.DARK.getColor());
        searchButton.setFocusable(false);

        emptyPanel.setBackground(MolvenoColor.LIGHT.getColor());
        Dimension separatorSize = new Dimension(850, 20);
        emptyPanel.setPreferredSize(separatorSize);
        emptyPanel.setMinimumSize(separatorSize);
        emptyPanel.setMaximumSize(separatorSize);
    }

    private void styleScrollPane() {
        scrollPane.setBackground(MolvenoColor.LIGHT.getColor());
    }

    private void styleTable() {
        ResultsTable.setBackground(MolvenoColor.LIGHT.getColor());
        ResultsTable.setForeground(MolvenoColor.DARK.getColor());
        ResultsTable.setFont(MolvenoFont.PARAGRAPH.getFont());
        ResultsTable.setGridColor(MolvenoColor.BEIGE.getColor());

        ResultsTable.setSelectionBackground(MolvenoColor.ORANGE.getColor());
        ResultsTable.setSelectionForeground(MolvenoColor.DARK.getColor());
        ResultsTable.setFocusable(false);
        ResultsTable.setRowHeight(30);
        ResultsTable.getTableHeader().setPreferredSize(new Dimension(0, 30));

        javax.swing.table.DefaultTableCellRenderer paddingRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

                return this;
            }
        };
        for (int i = 0; i < ResultsTable.getColumnCount(); i++) {
            ResultsTable.getColumnModel().getColumn(i).setCellRenderer(paddingRenderer);
        }


        javax.swing.table.JTableHeader header = ResultsTable.getTableHeader();
        header.setOpaque(false);

        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(MolvenoColor.GREEN.getColor());
                setForeground(MolvenoColor.DARK.getColor());
                setFont(MolvenoFont.PARAGRAPH.getFont());
                setBorder(BorderFactory.createLineBorder(MolvenoColor.BEIGE.getColor()));
                setHorizontalAlignment(JLabel.CENTER);
                return this;
            }
        });

        javax.swing.table.TableColumnModel columnModel = ResultsTable.getColumnModel();


        columnModel.getColumn(0).setPreferredWidth(150); // Achternaam
        columnModel.getColumn(1).setPreferredWidth(100); // Voornaam
        columnModel.getColumn(2).setPreferredWidth(155);  // Kamernummer
        columnModel.getColumn(3).setPreferredWidth(195); // Telefoonnummer
        columnModel.getColumn(4).setPreferredWidth(200); // Email

        // 3. Optional: Prevent columns from auto-resizing and squishing your manual widths
        ResultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
}
