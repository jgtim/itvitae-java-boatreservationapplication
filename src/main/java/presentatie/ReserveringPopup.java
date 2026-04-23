package presentatie;

import data.BoatDataExcel;
import domain.BoatRecord;
import domain.CustomerRecord;
import domain.ReservationPopupMode;
import domain.ReservationRecord;
import presentatie.Calendar.CalendarDisplay;
import service.BoatService.BoatService;
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
    private JPanel newboatPanel;
    private JTextField boatIdInputField;
    private JLabel addedBoatsLabel;
    private JPanel errorPane;
    private JLabel errorLabel;
    private JPanel tijdInformatie;
    private JComboBox comboBoxVan;
    private JComboBox comboBoxTot;
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

    //constructor voor het aanmaken van nieuwe reserveringen
    public ReserveringPopup(ReservationService reservationService, BoatService boatService, CalendarDisplay display,
                            String van, String tot, LocalDate datum, ArrayList<String> reservedBoats, String selectedBoat) {
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
        calculateTimeSpan(this.DisplayTab.getCalendar().timeBoxes, van, tot);
        dateField.setText(datum.toString());
        allBoats = boatService.getBoatList();
        this.validIDs = Arrays.asList(
                allBoats.stream()
                        .map(BoatRecord::boatName)
                        .toArray(String[]::new)
        );
        this.reservationService = reservationService;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        //Add the boat that is selected on the calendar
        boatIDs.add(selectedBoat);
        addedBoatsLabel.setText("Added: " + boatIDs.toString());
        calculateCost();
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
    }

    //this calculates the cost of existing boats
    private void calculateCost() {
        price = 0.0;
        for(int i = 0; i < boatIDs.size(); i++){
            String newBoatID = boatIDs.get(i);
            double boatPrice15 = allBoats.get(validIDs.indexOf(newBoatID)).price15();
            double boatPrice30 = allBoats.get(validIDs.indexOf(newBoatID)).price30();
            price += (times30 * boatPrice30);
            if(add15){
                price += boatPrice15;
            }
            priceField.setText("€" + String.format("%.2f", price));
        }

    }

    private ReserveringPopup getReserveringPopup() {
        return this;
    }

    // Constructor voor oude reserveringen
    public ReserveringPopup(ReservationService reservationService, ReservationRecord reservationRecord) {
        this.reservationService = reservationService;
        currentMode = ReservationPopupMode.VIEWING;
        currentReservation = reservationRecord;
        boatIDs = (ArrayList<String>) reservationRecord.boatIDs().clone();
        reservedBoats = boatIDs;
        this.begintijd = reservationRecord.begintijd();
        this.eindtijd = reservationRecord.eindtijd();
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
        allBoats = boatDataHandler.read();
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
        comboBoxVan.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                begintijd = comboBoxVan.getSelectedItem().toString();
                calculateTimeSpan(OPEN_TIMES, begintijd, eindtijd);
                calculateCost();
            }
        });
        comboBoxTot.setModel(new DefaultComboBoxModel<>(OPEN_TIMES));
        comboBoxTot.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                eindtijd = comboBoxTot.getSelectedItem().toString();
                calculateTimeSpan(OPEN_TIMES, begintijd, eindtijd);
                calculateCost();
            }
        });

        // Populate the fields of the popup with the selected reservation
        dateField.setText(reservationRecord.datum());
        vanLabel.setText(begintijd);
        totLabel.setText(eindtijd);
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
        buttonDelete.addActionListener(e -> onDelete(reservationRecord));


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void calculateTimeSpan(String[] timeBoxes, String van, String tot) {
        List<String> timeList = Arrays.asList(timeBoxes);
        int tijdsduur = Arrays.copyOfRange(timeBoxes, timeList.indexOf(van), timeList.indexOf(tot)).length;
        if((tijdsduur & 1) != 0){ //check if odd
            tijdsduur -= 1;
            this.add15 = true;
        }
        else{
            this.add15 = false;
        }
        this. times30 = tijdsduur / 2;
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
                priceField.setText("€" + String.format("%.2f", price));
            }

        } catch (NumberFormatException ex) {
            //throw new RuntimeException(ex);
            errorLabel.setText("vul een getal in");
        }
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
<<<<<<< reservation_block

        //TODO change display
=======
>>>>>>> main
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
}
