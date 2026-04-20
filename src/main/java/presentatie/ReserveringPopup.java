package presentatie;

import data.BoatDataExcel;
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
    private JLabel addedBoatsLabel;
    private JPanel errorPane;
    private JLabel errorLabel;
    private JPanel tijdInformatie;
    private JComboBox comboBoxVan;
    private JComboBox comboBoxTot;
    private JTable availableBoatsTable;
    private JTable addedBoatsTable;
    private JPanel addBoatPane;
    private JTextField textField1;
    private JButton button1;
    private JButton button2;
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
    private JPanel newBoatPanel;
    private DefaultTableModel availableBoatsTableModel;
    private DefaultTableModel addedBoatsTableModel;
    private BoatService boatService;

    public ReserveringPopup(ReservationService reservationService, BoatService boatService, CalendarDisplay display,
                            String van, String tot, LocalDate datum, ArrayList<String> reservedBoats, String selectedBoat) {
        this.boatService = boatService;
        deletePane.remove(buttonDelete);
        getReserveringPopup().DisplayTab = display;
        currentMode = ReservationPopupMode.CREATING;
        this.DisplayTab = display;
        this.begintijd = van;
        this.eindtijd = tot;
        this.datum = datum;
        this.reservedBoats = reservedBoats; //boats that are already reserved at the given time
        vanLabel.setText(van);
        totLabel.setText(tot);
        List<String> timeList = Arrays.asList(this.DisplayTab.getCalendar().timeBoxes);
        int tijdsduur = Arrays.copyOfRange(this.DisplayTab.getCalendar().timeBoxes, timeList.indexOf(van), timeList.indexOf(tot)).length;
        if((tijdsduur & 1) != 0){ //check if odd
            tijdsduur -= 1;
            this.add15 = true;
        }
        else{
            this.add15 = false;
        }
        times30 = tijdsduur / 2;
        dateField.setText(datum.toString());
        ArrayList<BoatRecord> allBoats = boatService.getBoatList();

        this.reservationService = reservationService;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        //Add the boat that is selected on the calendar
        boatIDs.add(selectedBoat);
        addedBoatsLabel.setText("Added: " + boatIDs.toString());

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
        addBoatButton.addActionListener(e -> {addBoat();});
        removeBoatButton.addActionListener(e -> {removeBoat();});
        initializeBoatTables();
        reloadBoatTables();
    }

    private ReserveringPopup getReserveringPopup() {
        return this;
    }

    // Constructor voor gebruik in reserveringen tab om een reservering in te zien of te wijzigen
    public ReserveringPopup(ReservationService reservationService, ReservationRecord reservationRecord) {
        this.reservationService = reservationService;
        currentMode = ReservationPopupMode.VIEWING;
        currentReservation = reservationRecord;
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

        var boatDataHandler = new BoatDataExcel();
        allBoats = boatDataHandler.read();
        boatService = new BoatServiceImpl(allBoats);

        setContentPane(contentPane);
        setModal(true);
//        getRootPane().setDefaultButton(buttonOK);


        comboBoxVan.setModel(new DefaultComboBoxModel<>(OPEN_TIMES));
        comboBoxTot.setModel(new DefaultComboBoxModel<>(OPEN_TIMES));

        // Populate the fields of the popup with the selected reservation
        dateField.setText(reservationRecord.datum());
        vanLabel.setText(reservationRecord.begintijd());
        totLabel.setText(reservationRecord.eindtijd());
        addedBoatsLabel.setText("Toegevoegd: " + reservationRecord.boatIDs().toString());
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
        boatIdInputField.setEditable(false);
        addBoatButton.setEnabled(false);
        removeBoatButton.setEnabled(false);

        addBoatButton.addActionListener(e -> {addBoat();});
        removeBoatButton.addActionListener(e -> {removeBoat();});

        buttonOK.setText("Wijzig");
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());
        this.price = reservationRecord.prijs();
        priceField.setText("€" + String.format("%.2f", price));
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


    private void setEditMode() {
        currentMode = ReservationPopupMode.EDITING;
        buttonOK.setText("Bevestig");
        comboBoxVan.setSelectedItem(vanLabel.getText());
        comboBoxTot.setSelectedItem(totLabel.getText());
        CardLayout cl = (CardLayout) tijdInformatie.getLayout();
        cl.next(tijdInformatie);
        boatIdInputField.setEditable(true);
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

    private void addBoat() {
        this.validIDs = Arrays.asList(
                allBoats.stream()
                        .map(BoatRecord::boatName)
                        .toArray(String[]::new)
        );

        try{
            String newBoatID = boatIdInputField.getText();
            if(!validIDs.contains(newBoatID)){
                errorLabel.setText("Die boot bestaat niet");
            }else if(reservedBoats.contains(newBoatID)){
                errorLabel.setText("Die boot is al gereserveerd");
            }else {
                boatIDs.add(newBoatID);
                addedBoatsLabel.setText("Toegevoegd: " + boatIDs.toString());
                boatIdInputField.setText("");
                errorLabel.setText(" ");
                double boatPrice15 = allBoats.get(validIDs.indexOf(newBoatID)).price15();
                double boatPrice30 = allBoats.get(validIDs.indexOf(newBoatID)).price30();
                price += (times30 * boatPrice30);
                if(add15){
                    price += boatPrice15;
                }
                //System.out.println(tijdsduur);
                System.out.println("PRIJS: " + boatPrice30 + "+" + boatPrice15);
                priceField.setText("€" + String.format("%.2f", price));
            }//TODO don't add if there is no boat with the given ID

        } catch (NumberFormatException ex) {
            //throw new RuntimeException(ex);
            errorLabel.setText("vul een getal in");
        }
        reloadBoatTables();
    }

    private void removeBoat() {
        try{
            String boatID = boatIdInputField.getText();
            if (boatIDs.contains(boatID)) {
                boatIDs.remove((Object) boatID);
                addedBoatsLabel.setText("Added: " + boatIDs.toString());
                boatIdInputField.setText("");
                errorLabel.setText(" ");
            } else {
                errorLabel.setText("Die boot is niet gereserveerd");
            }
        } catch (NumberFormatException ex) {
            errorLabel.setText("vul een getal in");
        }
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
            confirmEdit();
            setViewMode();
        }
    }

    private void addReservation() {
        ReservationRecord newReservation = getReservationRecord();

        reservationService.addReservation(newReservation);
        this.DisplayTab.reserveBoat(boatIDs, newReservation.reserveringsNummer());
        dispose();
    }

    private ReservationRecord getReservationRecord() {
        return new ReservationRecord(
                reservationService.getNewReservationNumber(),
                new CustomerRecord(
                        nameField.getText(),
                        (phoneNumberField.getText().isEmpty()) ? null : Integer.parseInt(phoneNumberField.getText().replace(" ", "")),
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

    private void onDelete(ReservationRecord ReservationRecord){
        reservationService.deleteReservation(ReservationRecord);
        dispose();
    }

    private void onDelete(ReservationRecord ReservationRecord, CalendarDisplay display) {
        reservationService.deleteReservation(ReservationRecord);
        //TODO change display
        dispose();
    }

    private boolean isFormValid() {
        if (nameField.getText().isEmpty()) {
            errorLabel.setText("geen naam ingevuld");
            return false;
        } else if (phoneNumberField.getText().isEmpty() && emailField.getText().isEmpty()) {
            errorLabel.setText("vul telefoonnummer en/of email in");
            return false;
        } else if (boatIDs.isEmpty()){
            errorLabel.setText("Er zijn geen boten gereserveerd");
            return false;
        }
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
    }

    private void reloadBoatTables() {
        Object[] row;
        BoatType[] boatTypes = BoatType.class.getEnumConstants();
        // Load available boats table
        availableBoatsTableModel.setRowCount(0);
        for (BoatType boatType : boatTypes) {
            row = new Object[3];
            row[0] = boatType;
            row[1] = boatService.getBoatCapacityFromType(boatType);
            row[2] = reservationService.getBoatAmountAvailableDuringTimeByType(
                    boatType,
                    begintijd,
                    eindtijd,
                    boatService
            );
            availableBoatsTableModel.addRow(row);
        }

        // reload added boats table
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

    }
}
