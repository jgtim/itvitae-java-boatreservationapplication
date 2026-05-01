package presentatie;

import data.BoatDataExcel;
import domain.*;
import presentatie.Calendar.CalendarDisplay;
import service.BoatService.BoatService;
import service.BoatService.BoatServiceImpl;
import service.ReservationService.ReservationService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ReserveringPopup extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel inputPane;
    private JTextField nameField;
    private JTextField phoneNumberField;
    private JCheckBox kamerFacturatieCheckBox;
    private JPanel agendaTijdInformatie;
    private JCheckBox betaaldCheckBox;
    private JPanel confirmationButtonsPanel;
    private JTextField emailField;
    private JLabel vanLabel;
    private JLabel totLabel;
    private CalendarDisplay DisplayTab;
    private JPanel dateInfo;
    private JLabel dateLabel;
    private JLabel dateField;
    private JButton addBoatButton;
    private JPanel errorPane;
    private JLabel errorLabel;
    private JPanel tijdInformatie;
    private JComboBox comboBoxVan;
    private JComboBox comboBoxTot;
    private JTable availableBoatsTable;
    private JTable addedBoatsTable;
    private JPanel addBoatPane;
    private JTextField boatAmountField;
    private JButton HotelDataButton;
    private JButton removeBoatButton;
    private JPanel deletePane;
    private JButton buttonDelete;
    private JLabel priceField;
    private JTextField roomNumberField;
    private JPanel botenPanel;
    private JScrollPane availableBoatsScrollPane;
    private JScrollPane addedBoatsScrollPane;
    private JPanel beschikbaarPanel;
    private JPanel toegevoegdPanel;
    private JLabel nameLabel;
    private JLabel phoneNumberLabel;
    private JLabel kamerFacturatieLabel;
    private JLabel betaaldLabel;
    private JLabel emailLabel;
    private JLabel priceLabel;
    private JLabel VanLabel;
    private JLabel TotLabel;
    private JLabel Van2Label;
    private JLabel Tot2Label;
    private JLabel BotenLabel;
    private JLabel beschikbaarLabel;
    private JLabel toegevoegdLabel;
    private JLabel aantalLabel;
    private JPanel comboPanel;
    private JLabel kamerLabel;
    private LocalDate datum;
    private String begintijd;
    private String eindtijd;
    private ArrayList<String> boatIDs = new ArrayList<String>();
    private ReservationService reservationService;
    private double price = 0.00;
    private int times30;
    private boolean add15;
    private List<String> validIDs;
    private ArrayList<BoatRecord> allBoats;
    private ArrayList<String> reservedBoats;
    private ReservationPopupMode currentMode;
    private ReservationRecord currentReservation;
    private DefaultTableModel availableBoatsTableModel;
    private DefaultTableModel addedBoatsTableModel;
    private BoatService boatService;
    private String preSelectedBoat = "";
    private BoatType selectedBoatType = null;
    private String dateString;
    List<String> timeList;

    //constructor voor het aanmaken van nieuwe reserveringen
    public ReserveringPopup(ReservationService reservationService, BoatService boatService, CalendarDisplay display,
                            String van, String tot, LocalDate datum, ArrayList<String> reservedBoats, String preSelectedBoat) {
        this.boatService = boatService;
        deletePane.remove(buttonDelete);
        getReserveringPopup().DisplayTab = display;
        currentMode = ReservationPopupMode.CREATING;
        this.DisplayTab = display;
        this.begintijd = van;
        this.eindtijd = tot;
        this.datum = datum;
        dateString = datum.toString();
        this.reservedBoats = reservedBoats; //boats that are already reserved at the given time
        this.preSelectedBoat = preSelectedBoat;
        vanLabel.setText(van);
        totLabel.setText(tot);
        calculateTimeSpan(this.DisplayTab.getCalendar().timeBoxes, van, tot);
        dateField.setText(datum.toString());
        var boatDataHandler = new BoatDataExcel();
        allBoats = boatDataHandler.read();
        this.validIDs = Arrays.asList(
                allBoats.stream()
                        .map(BoatRecord::boatName)
                        .toArray(String[]::new)
        );
        this.reservationService = reservationService;
        setContentPane(contentPane);
        setModal(true);
        // getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        addBoatButton.addActionListener(e -> addBoatButtonPressed());
        removeBoatButton.addActionListener(e -> removeBoatButtonPressed());

        HotelDataButton.addActionListener(e -> searchButtonPressed());

        roomNumberField.setEditable(false);
        roomNumberField.setEnabled(false);

        initializeBoatTables();
        reloadBoatTables();

        //Add the boat that is selected on the calendar
        selectedBoatType = boatService.getBoatTypeFromID(preSelectedBoat);
        addBoat();
        addedBoatsTable.setRowSelectionInterval(0, 0);

        styleBackgrounds();
        styleButtons();
        styleTables();
        styleComponents();
        // applyBoatTypeRenderer();

        setWindowIcon();

        this.pack();
        this.setLocationRelativeTo(null);
    }



    // Constructor voor bestaande reserveringen
    public ReserveringPopup(ReservationService reservationService, ReservationRecord reservationRecord) {
        this.reservationService = reservationService;

        this.getRootPane().setDefaultButton(buttonOK);
        currentMode = ReservationPopupMode.VIEWING;

        //TODO REMOVE
        contentPane.remove(HotelDataButton);
        currentMode = ReservationPopupMode.VIEWING;
        currentReservation = reservationRecord;
        dateString = currentReservation.datum();
        boatIDs = (ArrayList<String>) reservationRecord.boatIDs().clone();
        reservedBoats = boatIDs;
        begintijd = reservationRecord.begintijd();
        eindtijd = reservationRecord.eindtijd();
        String[] OPEN_TIMES = {
                "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45",
                "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45",
                "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45",
                "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45",
                "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45",
                "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45",
                "21:00", "21:15", "21:30"
        };

        calculateTimeSpan(OPEN_TIMES, reservationRecord.begintijd(), reservationRecord.eindtijd());
        var boatDataHandler = new BoatDataExcel();
        boatService = new BoatServiceImpl(boatDataHandler.read());
        allBoats = boatService.getBoatList();
        this.validIDs = Arrays.asList(
                allBoats.stream()
                        .map(BoatRecord::boatName)
                        .toArray(String[]::new)
        );
        calculateCost();
        setContentPane(contentPane);
        setModal(true);
//        getRootPane().setDefaultButton(buttonOK);


        comboBoxVan.setModel(new DefaultComboBoxModel<>(OPEN_TIMES));
        comboBoxVan.addActionListener(e -> {
            begintijd = comboBoxVan.getSelectedItem().toString();
            calculateTimeSpan(OPEN_TIMES, begintijd, eindtijd);
            calculateCost();
            reloadSelectableTimes();
            comboBoxVan.setSelectedItem(begintijd);
        });
        comboBoxTot.setModel(new DefaultComboBoxModel<>(OPEN_TIMES));
        comboBoxTot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eindtijd = comboBoxTot.getSelectedItem().toString();
                calculateTimeSpan(OPEN_TIMES, begintijd, eindtijd);
                calculateCost();
            }
        });

        // Populate the fields of the popup with the selected reservation
        dateField.setText(reservationRecord.datum());
        vanLabel.setText(reservationRecord.begintijd());
        totLabel.setText(reservationRecord.eindtijd());
        nameField.setText(reservationRecord.customerRecord().naam());
        nameField.setEditable(false);
        phoneNumberField.setText((reservationRecord.customerRecord().telefoonNummer() != null) ? reservationRecord.customerRecord().telefoonNummer().toString() : "");
        phoneNumberField.setEditable(false);
        roomNumberField.setText((reservationRecord.customerRecord().kamerNummer() != null) ? reservationRecord.customerRecord().kamerNummer().toString() : "");
        roomNumberField.setEditable(false);
        roomNumberField.setEnabled(false);
        emailField.setText(reservationRecord.customerRecord().email());
        emailField.setEditable(false);
        kamerFacturatieCheckBox.setSelected(reservationRecord.kamerFacturatie());
        kamerFacturatieCheckBox.setEnabled(false);
        betaaldCheckBox.setSelected(reservationRecord.betaald());
        betaaldCheckBox.setEnabled(false);
        addBoatButton.setEnabled(false);
        removeBoatButton.setEnabled(false);


        addBoatButton.addActionListener(e -> {addBoatButtonPressed();});
        removeBoatButton.addActionListener(e -> {removeBoatButtonPressed();});

        buttonOK.setText("Wijzig");
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());
        buttonDelete.addActionListener(e -> onDelete(reservationRecord));


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        initializeBoatTables();
        reloadBoatTables();
        reloadSelectableTimes();

        styleBackgrounds();
        styleButtons();
        styleTables();
        styleComponents();

        setWindowIcon();

        this.pack();
        this.setLocationRelativeTo(null);
        // applyBoatTypeRenderer();
    }

    //this calculates the cost of existing boats
    private void calculateCost() {
        price = 0.0;
        for (int i = 0; i < boatIDs.size(); i++) {
            String newBoatID = boatIDs.get(i);
            double boatPrice15 = allBoats.get(validIDs.indexOf(newBoatID)).price15();
            double boatPrice30 = allBoats.get(validIDs.indexOf(newBoatID)).price30();
            price += (times30 * boatPrice30);
            if (add15) {
                price += boatPrice15;
            }
        }
        priceField.setText("€" + String.format("%.2f", price));
    }

    private ReserveringPopup getReserveringPopup() {
        return this;
    }

    private void calculateTimeSpan(String[] timeBoxes, String van, String tot) {
        this.timeList = Arrays.asList(timeBoxes);
        int tijdsduur;
        if (timeList.indexOf(van) < timeList.indexOf(tot)) {
            tijdsduur = Arrays.copyOfRange(timeBoxes, timeList.indexOf(van), timeList.indexOf(tot)).length;
        } else {
            tijdsduur = 0;
        }
        if ((tijdsduur & 1) != 0) { //check if odd
            tijdsduur -= 1;
            this.add15 = true;
        } else {
            this.add15 = false;
        }
        this.times30 = tijdsduur / 2;
    }

    private void setEditMode() {
        currentMode = ReservationPopupMode.EDITING;
        buttonOK.setText("Bevestig");
        comboBoxVan.setSelectedItem(vanLabel.getText());
        comboBoxTot.setSelectedItem(totLabel.getText());
        CardLayout cl = (CardLayout) tijdInformatie.getLayout();
        cl.next(tijdInformatie);
        addBoatButton.setEnabled(true);
        removeBoatButton.setEnabled(true);
        roomNumberField.setEditable(false);
        roomNumberField.setEnabled(false);
    }

    private void setViewMode() {
        if (currentMode == ReservationPopupMode.EDITING) {
            currentMode = ReservationPopupMode.VIEWING;
            CardLayout cl = (CardLayout) tijdInformatie.getLayout();
            cl.next(tijdInformatie);
            buttonOK.setText("Wijzig");
            addBoatButton.setEnabled(false);
            removeBoatButton.setEnabled(false);
            roomNumberField.setEditable(false);
            roomNumberField.setEnabled(false);
        }
    }

    private void confirmEdit() {
        // Check if new boats are overlapping with existing ones
        boolean isOverLapping = false;
        for (String boatID : new ArrayList<>(boatIDs)) {
            if (reservationService.isBoatIDOverLapping(boatID, dateString, begintijd, eindtijd, currentReservation.reserveringsNummer())) {
                boatIDs.remove(boatID);
                boatIDs.add(reservationService.getFirstAvailableBoatOfType(boatService.getBoatTypeFromID(boatID), boatIDs, boatService, begintijd, eindtijd, dateString));
            }
        }

        ReservationRecord updatedReservation = currentReservation.toBuilder()
                .begintijd(comboBoxVan.getSelectedItem().toString())
                .eindtijd(comboBoxTot.getSelectedItem().toString())
                .boatIDs(boatIDs)
                .build();
        reservationService.updateReservation(updatedReservation);
        vanLabel.setText((String) comboBoxVan.getSelectedItem());
        totLabel.setText((String) comboBoxTot.getSelectedItem());
    }

    private void addBoatButtonPressed() {
        if (selectedBoatType == null) {
            errorLabel.setText("Geen boot type geselecteerd");
        } else if (reservationService.getBoatAmountAvailableDuringTimeByType(selectedBoatType, begintijd, eindtijd, dateString, boatService, boatIDs) <=0) {
            errorLabel.setText("Maximale van boot type al geselecteerd");
        } else {
            if (boatAmountField.getText().isEmpty()) {
                addBoat();
            }
            try {
                int amountToAdd = Integer.parseInt(boatAmountField.getText());
                for (int i = 0; i < amountToAdd; i++) {
                    addBoat();
                }
            } catch (NumberFormatException ignored) {
            }
            reloadSelectableTimes();
            errorLabel.setText("");
        }
    }

    private void addBoat() {
        // checks if its the boat that was clicked on the agenda
        if (!preSelectedBoat.isEmpty()) {
            boatIDs.add(preSelectedBoat);
            preSelectedBoat = "";
        } else {
            if (reservationService.getBoatAmountAvailableDuringTimeByType(selectedBoatType, begintijd, eindtijd, dateString, boatService, boatIDs) > 0) {
                String firstAvailable = reservationService.getFirstAvailableBoatOfType(selectedBoatType, boatIDs, boatService, begintijd, eindtijd, dateString);
                boatIDs.add(firstAvailable);
                errorLabel.setText("");
            } else {
                errorLabel.setText("Maximale van boot type al geselecteerd");
            }
        }

        calculateCost();
        reloadBoatTables();
    }

    private void removeBoatButtonPressed() {
        if (selectedBoatType != null &&
                getAmountOfSelectedBoatsFromType(selectedBoatType) > 0) {
            if (boatAmountField.getText().isEmpty()) {
                removeBoat();
            } else {
                try {
                    int amountToRemove = Integer.parseInt(boatAmountField.getText());
                    for (int i = 0; i < amountToRemove; i++) {
                        removeBoat();
                    }
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }
            }
            errorLabel.setText("");
        } else {
            errorLabel.setText("Geen boot type geselecteerd");
        }
    }

    private void removeBoat() {
        for (int i = boatIDs.size() - 1; i >= 0; i--) {
            if (boatService.getBoatTypeFromID(boatIDs.get(i)) == selectedBoatType &&
                    getAmountOfSelectedBoatsFromType(selectedBoatType) > 0) {
                boatIDs.remove(i);
                if (getAmountOfSelectedBoatsFromType(selectedBoatType) <= 0) {
                    selectedBoatType = null;
                    addedBoatsTable.clearSelection();
                    availableBoatsTable.clearSelection();
                }
                calculateCost();
                break;
            }
        }

        reloadBoatTables();
        reloadSelectableTimes();
    }

    private void searchButtonPressed() {
        HotelGuestSearch searchWindow = new HotelGuestSearch(){
            @Override
            public void dispose() {
                super.dispose();
                HotelGuestRecord currentGuest = this.selectedGuest;
                nameField.setText(currentGuest.voornaam() + " " + currentGuest.achternaam());
                phoneNumberField.setText(currentGuest.telefoonnummer() == null ? "" : String.valueOf(currentGuest.telefoonnummer()));
                emailField.setText(currentGuest.email());
                kamerFacturatieCheckBox.setSelected(true);
                roomNumberField.setText(String.valueOf(currentGuest.kamernummer()));
            }
        };
        searchWindow.pack();
        searchWindow.setLocationRelativeTo(null);
        searchWindow.setVisible(true);
    }

    private void onOK() {
        if (currentMode == ReservationPopupMode.CREATING) {
            if (!isFormValid()) {
                return;
            }
            addReservation();
            dispose();
        } else if (currentMode == ReservationPopupMode.VIEWING) {
            setEditMode();
        } else if (currentMode == ReservationPopupMode.EDITING) {
            if (!isFormValid()) {
                return;
            }
            confirmEdit();
            setViewMode();

        }
    }

    private void addReservation() {
        ReservationRecord newReservation = getReservationRecord();

        reservationService.addReservation(newReservation);
        //this.DisplayTab.reserveBoat(boatIDs, newReservation.reserveringsNummer());
    }

    private ReservationRecord getReservationRecord() {
        return new ReservationRecord(
                reservationService.getNewReservationNumber(),
                new CustomerRecord(
                        nameField.getText(),
                        (phoneNumberField.getText().isBlank()) ? null : Integer.parseInt(phoneNumberField.getText().replace(" ", "")),
                        (emailField.getText().isEmpty()) ? null : emailField.getText(),
                        (roomNumberField.getText().isBlank()) ? null : Integer.parseInt(roomNumberField.getText().replace(" ", ""))

                ),
                betaaldCheckBox.isSelected(),
                kamerFacturatieCheckBox.isSelected(),
                datum.toString(),
                begintijd,
                eindtijd,
                boatIDs,
                price

                );
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onDelete(ReservationRecord ReservationRecord) {
        reservationService.deleteReservation(ReservationRecord);
        dispose();
    }

    private boolean isFormValid() {
        if (nameField.getText().isEmpty()) {
            errorLabel.setText("Geen naam ingevuld");
            return false;
        } else if (phoneNumberField.getText().isEmpty() && emailField.getText().isEmpty()) {
            errorLabel.setText("Vul telefoonnummer en/of email in");
            return false;
        } else if (boatIDs.isEmpty()) {
            errorLabel.setText("Er zijn geen boten gereserveerd");
            return false;
        } else if (!isANumber(phoneNumberField.getText()) && !phoneNumberField.getText().isBlank()) {
            errorLabel.setText("Telefoonnummer moet een getal zijn");
            return false;
        } else if (currentMode == ReservationPopupMode.EDITING && timeList.indexOf((String) comboBoxVan.getSelectedItem()) >= timeList.indexOf((String) comboBoxTot.getSelectedItem())) {
            errorLabel.setText("Begintijd moet lager zijn dan eindtijd");
            return false;
        } else if  (!isANumber(roomNumberField.getText()) && !roomNumberField.getText().isBlank()) {
            errorLabel.setText("Kamernummer moet een getal zijn");
            return false;
        }
        errorLabel.setText("");
        return true;
    }

    private void initializeBoatTables() {
        String[] columnNamesAvailable = {"Type", "Capaciteit", "Beschikbaar"};
        String[] columnNamesAdded = {"Type", "Capaciteit", "Toegevoegd"};

        String[][] tableData = {};

        availableBoatsTableModel = new DefaultTableModel(tableData, columnNamesAvailable) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        addedBoatsTableModel = new DefaultTableModel(tableData, columnNamesAdded) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        availableBoatsTable.setModel(availableBoatsTableModel);
        addedBoatsTable.setModel(addedBoatsTableModel);

        availableBoatsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addedBoatsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        availableBoatsTable.getTableHeader().setReorderingAllowed(false);
        addedBoatsTable.getTableHeader().setReorderingAllowed(false);

        availableBoatsTable.getTableHeader().setResizingAllowed(false);
        addedBoatsTable.getTableHeader().setResizingAllowed(false);

        addedBoatsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                availableBoatsTable.clearSelection();
                int viewRow = addedBoatsTable.rowAtPoint(e.getPoint());
                selectedBoatType = BoatType.getBoatTypeFromDisplayName(
                        (String) addedBoatsTable.getModel().getValueAt(viewRow, 0));
            }
        });

        availableBoatsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                addedBoatsTable.clearSelection();
                int viewRow = availableBoatsTable.rowAtPoint(e.getPoint());
                selectedBoatType = BoatType.getBoatTypeFromDisplayName(
                        (String) availableBoatsTable.getModel().getValueAt(viewRow, 0));
            }
        });
    }

    private void reloadBoatTables() {
        Object[] row;
        BoatType[] boatTypes = BoatType.class.getEnumConstants();
        // Load available boats table
        int selectedrow = availableBoatsTable.getSelectedRow();
        availableBoatsTableModel.setRowCount(0);
        for (BoatType boatType : boatTypes) {
            row = new Object[3];
            row[0] = boatType.getDisplayBoatType();
            row[1] = boatService.getBoatCapacityFromType(boatType);
            row[2] = reservationService.getBoatAmountAvailableDuringTimeByType(
                    boatType,
                    begintijd,
                    eindtijd,
                    dateString,
                    boatService,
                    boatIDs
            );
            availableBoatsTableModel.addRow(row);
        }
        try {
            availableBoatsTable.setRowSelectionInterval(selectedrow, selectedrow);
        } catch (Exception ignored) {
        }


        // reload added boats table
        selectedrow = addedBoatsTable.getSelectedRow();
        addedBoatsTableModel.setRowCount(0);
        for (BoatType boatType : boatTypes) {
            row = new Object[3];
            row[0] = boatType.getDisplayBoatType();
            row[1] = boatService.getBoatCapacityFromType(boatType);
            row[2] = 0;
            // check how many boats of this type have been added
            for (String boatID : boatIDs) {
                if (boatService.getBoatTypeFromID(boatID) == boatType) {
                    row[2] = (int) row[2] + 1;
                }
            }
            if ((int) row[2] > 0) {
                addedBoatsTableModel.addRow(row);
            }
        }

        try {
            addedBoatsTable.setRowSelectionInterval(selectedrow, selectedrow);
        } catch (Exception ignored) {
        }

    }

    public static boolean isANumber(String phoneNum) {
        try {
            Integer.parseInt(phoneNum);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void onDelete(ReservationRecord ReservationRecord, CalendarDisplay display) {
        reservationService.deleteReservation(ReservationRecord);

        //TODO change display

        dispose();
    }

    public void reloadSelectableTimes() {
        // For each element in the StartTimes combobox check if its possible to switch to
        var startTimes = new ArrayList<String>(timeList);
        BoatType[] boatTypes = BoatType.class.getEnumConstants();
        for (String time : timeList) {
            for (BoatType boatType : boatTypes) {
                int availableAmount = reservationService.getBoatAmountAvailableDuringTimeByType(
                        boatType,
                        time,
                        LocalTime.parse(time).plusMinutes(15).toString(),
                        dateString,
                        boatService,
                        new ArrayList<String>(),
                        currentReservation
                );
                if (availableAmount < getAmountOfSelectedBoatsFromType(boatType)) {
                    startTimes.remove(time);
                }
            }
        }
        comboBoxVan.setModel(new DefaultComboBoxModel<>(startTimes.toArray()));

        // For each element in the EndTimes combobox check if its possible to switch to
        var endTimes = new ArrayList<String>(timeList);
        for (String time : timeList) {
            for (BoatType boatType : boatTypes) {
                int availableAmount = reservationService.getBoatAmountAvailableDuringTimeByType(
                        boatType,
                        begintijd,
                        time,
                        dateString,
                        boatService,
                        new ArrayList<String>(),
                        currentReservation
                );
                if (availableAmount < getAmountOfSelectedBoatsFromType(boatType)) {
                    endTimes.remove(time);
                }
                if (LocalTime.parse(time).isBefore(LocalTime.parse(begintijd))) {
                    endTimes.remove(time);
                }
            }
        }
        comboBoxTot.setModel(new DefaultComboBoxModel<>(endTimes.toArray()));


    }

    private int getAmountOfSelectedBoatsFromType (BoatType boatType) {
        int result = 0;
        for (String boatID : boatIDs) {
            if (boatService.getBoatTypeFromID(boatID) == boatType) {
                result ++;
            }
        }
        return result;
    }

    // Styling -------------------------------------------------------------------------------------------------------
    private void styleComponents() {
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inputPane.setPreferredSize(new Dimension(200, 400));
        addBoatPane.setPreferredSize(new Dimension(700, 400));

        for (JTable table : Arrays.asList(availableBoatsTable, addedBoatsTable)) {
            table.getColumnModel().getColumn(0).setPreferredWidth(200);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        List<JTextField> fields = Arrays.asList(nameField, phoneNumberField, emailField, boatAmountField, roomNumberField);
        int padding = 5;

        for (JTextField field : fields) {
            field.setBackground(MolvenoColor.LIGHT.getColor());
            field.setForeground(MolvenoColor.DARK.getColor());
            field.setFont(MolvenoFont.PARAGRAPH.getFont());
            field.setBorder(BorderFactory.createLineBorder(MolvenoColor.BEIGE.getColor()));

            Border lineBorder = BorderFactory.createLineBorder(MolvenoColor.BEIGE.getColor());
            Border margin = BorderFactory.createEmptyBorder(padding, padding, padding, padding);
            field.setBorder(BorderFactory.createCompoundBorder(lineBorder, margin));
        }

        List<JLabel> labels = Arrays.asList(kamerLabel, phoneNumberLabel, dateField, aantalLabel, beschikbaarLabel, toegevoegdLabel, vanLabel, VanLabel, Van2Label, TotLabel, Tot2Label, totLabel, priceLabel, priceField, emailLabel, nameLabel, kamerFacturatieLabel, betaaldLabel);
        for (JLabel label : labels) {
            if (label != null) {
                label.setForeground(MolvenoColor.DARK.getColor());
                label.setFont(MolvenoFont.PARAGRAPH.getFont().deriveFont(Font.BOLD));
            }
        }

        List<JScrollPane> scrollPanes = Arrays.asList(availableBoatsScrollPane, addedBoatsScrollPane);

        for (JScrollPane pane : scrollPanes) {
            if (pane != null) {
                pane.setBackground(MolvenoColor.LIGHT.getColor());
                pane.getViewport().setBackground(MolvenoColor.LIGHT.getColor());
                pane.setBorder(BorderFactory.createLineBorder(MolvenoColor.BEIGE.getColor()));
            }
        }
        availableBoatsScrollPane.setPreferredSize(new Dimension(700, 200));
        addedBoatsScrollPane.setPreferredSize(new Dimension(700, 150));

        availableBoatsScrollPane.revalidate();
        addedBoatsScrollPane.revalidate();
        contentPane.revalidate();

        List<JCheckBox> checkBoxes = Arrays.asList(kamerFacturatieCheckBox, betaaldCheckBox);
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox != null) {
                checkBox.setBackground(MolvenoColor.LIGHT.getColor());
                checkBox.setForeground(MolvenoColor.DARK.getColor());
                checkBox.setFont(MolvenoFont.PARAGRAPH.getFont());
                checkBox.setFocusPainted(false);
            }
        }

        List<JComboBox> combos = Arrays.asList(comboBoxVan, comboBoxTot);
        for (JComboBox combo : combos) {
            if (combo != null) {
                combo.setBackground(MolvenoColor.LIGHT.getColor());
                combo.setForeground(MolvenoColor.DARK.getColor());
                combo.setFont(MolvenoFont.PARAGRAPH.getFont());
                combo.setBorder(BorderFactory.createLineBorder(MolvenoColor.BEIGE.getColor()));

                Object child = combo.getAccessibleContext().getAccessibleChild(0);
                if (child instanceof javax.swing.plaf.basic.BasicComboPopup) {
                    javax.swing.plaf.basic.BasicComboPopup popup = (javax.swing.plaf.basic.BasicComboPopup) child;
                    popup.getList().setBackground(MolvenoColor.LIGHT.getColor());
                    popup.getList().setSelectionBackground(MolvenoColor.ORANGE.getColor());
                }

                ((JLabel)combo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

                combo.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        label.setHorizontalAlignment(SwingConstants.CENTER);

                        if (isSelected) {
                            label.setBackground(MolvenoColor.ORANGE.getColor());
                            label.setForeground(MolvenoColor.DARK.getColor());
                        } else {
                            label.setBackground(MolvenoColor.LIGHT.getColor());
                            label.setForeground(MolvenoColor.DARK.getColor());
                        }
                        return label;
                    }
                });
            }
        }
    }

    private void styleTables () {
        for (JTable table : Arrays.asList(availableBoatsTable, addedBoatsTable)) {
            table.setRowHeight(25);
            table.setIntercellSpacing(new Dimension(10, 5));

            table.setBackground(MolvenoColor.LIGHT.getColor());
            table.setGridColor(MolvenoColor.BEIGE.getColor());
            table.setSelectionBackground(MolvenoColor.ORANGE.getColor());
            table.setSelectionForeground(MolvenoColor.DARK.getColor());
            table.setFont(MolvenoFont.PARAGRAPH.getFont());
            table.setFocusable(false);


            javax.swing.table.JTableHeader header = table.getTableHeader();
            header.setFont(MolvenoFont.PARAGRAPH.getFont());

            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    setBackground(MolvenoColor.GREEN.getColor());
                    setForeground(MolvenoColor.DARK.getColor());
                    setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, MolvenoColor.BEIGE.getColor()));
                    setHorizontalAlignment(JLabel.CENTER);

                    return this;
                }
            });
        }
    }

    private void applyBoatTypeRenderer(JTable table) {
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof BoatType) {
                    setText(((BoatType) value).getDisplayBoatType());
                } else {
                    super.setValue(value);
                }
            }
        });
    }

    private void styleButtons() {
        List<JButton> buttons = Arrays.asList(
                buttonOK,
                buttonCancel,
                addBoatButton,
                removeBoatButton,
                buttonDelete,
                HotelDataButton
        );

        for (JButton btn : buttons) {
            if (btn == null) continue;

            btn.setBackground(MolvenoColor.BEIGE.getColor());
            btn.setForeground(MolvenoColor.DARK.getColor());
            btn.setFont(MolvenoFont.BUTTONLABEL.getFont());
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(MolvenoColor.BLUE.getColor());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(MolvenoColor.BEIGE.getColor());
                }
            });
        }

        if (buttonDelete != null) {
            buttonDelete.setBackground(MolvenoColor.ORANGE.getColor());
            buttonDelete.setForeground(MolvenoColor.DARK.getColor());
        }
    }

    private void styleBackgrounds() {
        contentPane.setBackground(MolvenoColor.LIGHT.getColor());

        List<JPanel> panels = Arrays.asList(
                inputPane,
                agendaTijdInformatie,
                confirmationButtonsPanel,
                dateInfo,
                errorPane,
                tijdInformatie,
                addBoatPane,
                deletePane,
                botenPanel,
                toegevoegdPanel,
                beschikbaarPanel,
                contentPane,
                comboPanel
        );

        for (JPanel panel : panels) {
            if (panel != null) {
                panel.setBackground(MolvenoColor.LIGHT.getColor());
                panel.setOpaque(true);
            }
        }
        contentPane.setPreferredSize(new Dimension(900, 600));
    }

    private void setWindowIcon() {
        ImageIcon logo = new ImageIcon(App.class.getResource("/images/MolvenoLogo.png"));
        setIconImage(logo.getImage());
    }
}