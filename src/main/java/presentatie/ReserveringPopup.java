package presentatie;

import data.BoatDataExcel;
import data.BoatDataHandler;
import domain.*;
import presentatie.Calendar.CalendarDisplay;
import service.BoatService.BoatService;
import service.BoatService.BoatServiceImpl;
import service.ReservationService.ReservationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    private JTextField boatIdInputField;
    private JPanel errorPane;
    private JLabel errorLabel;
    private JPanel tijdInformatie;
    private JComboBox comboBoxVan;
    private JComboBox comboBoxTot;
    private JTable availableBoatsTable;
    private JTable addedBoatsTable;
    private JPanel addBoatPane;
    private JTextField boatAmountField;
    private JButton button1;
    private JButton removeBoatButton;
    private JPanel deletePane;
    private JButton buttonDelete;
    private JLabel priceField;
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
    private BoatType selectedBoatType;
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
        getRootPane().setDefaultButton(buttonOK);

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
        addBoatButton.addActionListener(e -> {
            addBoatButtonPressed();
        });
        removeBoatButton.addActionListener(e -> {
            removeBoatButtonPressed();
        });
        initializeBoatTables();
        reloadBoatTables();
        //Add the boat that is selected on the calendar
        addBoat();
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
            priceField.setText("€" + String.format("%.2f", price));
        }

    }

    private ReserveringPopup getReserveringPopup() {
        return this;
    }

    // Constructor voor bestaande reserveringen
    public ReserveringPopup(ReservationService reservationService, ReservationRecord reservationRecord) {
        this.reservationService = reservationService;
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
        comboBoxVan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                begintijd = comboBoxVan.getSelectedItem().toString();
                calculateTimeSpan(OPEN_TIMES, begintijd, eindtijd);
                calculateCost();
            }
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
        phoneNumberField.setText(reservationRecord.customerRecord().telefoonNummer().toString());
        phoneNumberField.setEditable(false);
        emailField.setText(reservationRecord.customerRecord().email());
        emailField.setEditable(false);
        kamerFacturatieCheckBox.setSelected(reservationRecord.kamerFacturatie());
        kamerFacturatieCheckBox.setEnabled(false);
        betaaldCheckBox.setSelected(reservationRecord.betaald());
        betaaldCheckBox.setEnabled(false);
        addBoatButton.setEnabled(false);
        removeBoatButton.setEnabled(false);
        // Populate the fields of the popup with the selected reservation
        dateField.setText(reservationRecord.datum());
        vanLabel.setText(reservationRecord.begintijd());
        totLabel.setText(reservationRecord.eindtijd());
        nameField.setText(reservationRecord.customerRecord().naam());
        nameField.setEditable(false);
        phoneNumberField.setText((reservationRecord.customerRecord().telefoonNummer() != null) ? reservationRecord.customerRecord().telefoonNummer().toString() : "");
        phoneNumberField.setEditable(false);
        emailField.setText(reservationRecord.customerRecord().email());
        emailField.setEditable(false);
        kamerFacturatieCheckBox.setSelected(reservationRecord.kamerFacturatie());
        kamerFacturatieCheckBox.setEnabled(false);
        betaaldCheckBox.setSelected(reservationRecord.betaald());
        betaaldCheckBox.setEnabled(false);
        //boatIdInputField.setEditable(false);
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
    }

    private void setViewMode() {
        if (currentMode == ReservationPopupMode.EDITING) {
            currentMode = ReservationPopupMode.VIEWING;
            CardLayout cl = (CardLayout) tijdInformatie.getLayout();
            cl.next(tijdInformatie);
            buttonOK.setText("Wijzig");
            boatIdInputField.setEditable(false);
            addBoatButton.setEnabled(false);
            removeBoatButton.setEnabled(false);
        }
    }

    private void confirmEdit() {
        ReservationRecord updatedReservation = currentReservation.toBuilder()
                .begintijd(comboBoxVan.getSelectedItem().toString())
                .eindtijd(comboBoxTot.getSelectedItem().toString())
                .boatIDs(boatIDs)
                .build();
        System.out.println(updatedReservation);
        reservationService.updateReservation(updatedReservation);
        vanLabel.setText((String) comboBoxVan.getSelectedItem());
        totLabel.setText((String) comboBoxTot.getSelectedItem());
    }

    private void addBoatButtonPressed() {
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
    }

    private void addBoat() {
        String boatString;
        // checks if its the boat that was clicked on the agenda
        if (!preSelectedBoat.isEmpty()) {
            boatIDs.add(preSelectedBoat);
            boatString = preSelectedBoat;
            preSelectedBoat = "";
        } else {
            String firstAvailable = reservationService.getFirstAvailableBoatOfType(selectedBoatType, boatIDs, boatService, begintijd, eindtijd, dateString);
            boatIDs.add(firstAvailable);
            boatString = firstAvailable;
        }


        this.validIDs = Arrays.asList(
                allBoats.stream()
                        .map(BoatRecord::boatName)
                        .toArray(String[]::new)
        );

        System.out.println(validIDs.size());
        System.out.println(boatString);
        double boatPrice15 = 0;
        try {
            boatPrice15 = boatService.getBoatPrice15FromName(boatString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        double boatPrice30 = 0;
        try {
            boatPrice30 = boatService.getBoatPrice30FromName(boatString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        price += (times30 * boatPrice30);
        if (add15) {
            price += boatPrice15;
        }

        System.out.println("PRIJS: " + boatPrice30 + "+" + boatPrice15);
        priceField.setText("€" + String.format("%.2f", price));

        reloadBoatTables();
    }

    private void removeBoatButtonPressed() {
        if (boatAmountField.getText().isEmpty()) {
            removeBoat();
        }
        try {
            int amountToRemove = Integer.parseInt(boatAmountField.getText());
            for (int i = 0; i < amountToRemove; i++) {
                removeBoat();
            }
        } catch (NumberFormatException ignored) {
        }
    }

    private void removeBoat() {
        for (int i = boatIDs.size() - 1; i >= 0; i--) {
            if (boatService.getBoatTypeFromID(boatIDs.get(i)) == selectedBoatType) {
                boatIDs.remove(i);
                calculateCost();
                break;
            }
        }

        reloadBoatTables();
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
                        (emailField.getText().isEmpty()) ? null : emailField.getText()
                ),
                kamerFacturatieCheckBox.isSelected(),
                betaaldCheckBox.isSelected(),
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
            errorLabel.setText("geen naam ingevuld");
            return false;
        } else if (phoneNumberField.getText().isEmpty() && emailField.getText().isEmpty()) {
            errorLabel.setText("vul telefoonnummer en/of email in");
            return false;
        } else if (boatIDs.isEmpty()) {
            errorLabel.setText("Er zijn geen boten gereserveerd");
            return false;
        } else if (!isANumber(phoneNumberField.getText()) && !phoneNumberField.getText().isBlank()) {
            errorLabel.setText("Telefoonnummer moet een getal zijn");
            return false;
        } else if (currentMode == ReservationPopupMode.EDITING && timeList.indexOf((String) comboBoxVan.getSelectedItem()) >= timeList.indexOf((String) comboBoxTot.getSelectedItem())) {
            errorLabel.setText("begintijd moet lager zijn dan eindtijd");
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

        addedBoatsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                availableBoatsTable.clearSelection();
                int viewRow = addedBoatsTable.rowAtPoint(e.getPoint());
                selectedBoatType = (BoatType) addedBoatsTable.getModel().getValueAt(viewRow, 0);
            }
        });

        availableBoatsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addedBoatsTable.clearSelection();
                int viewRow = availableBoatsTable.rowAtPoint(e.getPoint());
                selectedBoatType = (BoatType) availableBoatsTable.getModel().getValueAt(viewRow, 0);
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
            row[0] = boatType;
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
            row[0] = boatType;
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

}
