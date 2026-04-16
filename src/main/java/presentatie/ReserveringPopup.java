package presentatie;

import domain.CustomerRecord;
import domain.ReservationPopupMode;
import domain.ReservationRecord;
import presentatie.Calendar.CalendarDisplay;
import service.ReservationService.ReservationService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private LocalDate datum;
    private String begintijd;
    private String eindtijd;
    private ArrayList<Integer> boatIDs = new ArrayList<Integer>();
    private ReservationService reservationService;
    private ArrayList<Integer> reservedBoats;
    private ReservationPopupMode currentMode;
    private ReservationRecord currentReservation;

    // Constructor voor aanmaken van reserveringen
    public ReserveringPopup(ReservationService reservationService, CalendarDisplay display, String van, String tot, LocalDate datum, ArrayList<Integer> reservedBoats) { //LocalDate date
        currentMode = ReservationPopupMode.CREATING;
        this.DisplayTab = display;
        this.begintijd = van;
        this.eindtijd = tot;
        this.datum = datum;
        this.reservedBoats = reservedBoats; //boats that are already reserved at the given time
        vanLabel.setText(van);
        totLabel.setText(tot);
        dateField.setText(datum.toString());
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
        addBoatButton.addActionListener(e -> {addBoat();});
        removeBoatButton.addActionListener(e -> {removeBoat();});
    }

    // Constructor voor gebruik in reserveringen tab om een reservering in te zien of te wijzigen
    public ReserveringPopup(ReservationService reservationService, ReservationRecord reservationRecord) {
        this.reservationService = reservationService;
        currentMode = ReservationPopupMode.VIEWING;
        currentReservation = reservationRecord;
        boatIDs = (ArrayList<Integer>) reservationRecord.boatIDs().clone();
        reservedBoats = boatIDs;
        String[] OPEN_TIMES = {
                "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45",
                "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45",
                "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45",
                "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45",
                "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45",
                "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45",
                "21:00", "21:15", "21:30"
        };

        setContentPane(contentPane);
        setModal(true);
//        getRootPane().setDefaultButton(buttonOK);


        comboBoxVan.setModel(new DefaultComboBoxModel<>(OPEN_TIMES));
        comboBoxTot.setModel(new DefaultComboBoxModel<>(OPEN_TIMES));

        // Populate the fields of the popup with the selected reservation
        dateField.setText(reservationRecord.datum());
        vanLabel.setText(reservationRecord.begintijd());
        totLabel.setText(reservationRecord.eindtijd());
        addedBoatsLabel.setText("Added: " + reservationRecord.boatIDs().toString());
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


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
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
            CardLayout cl = (CardLayout) tijdInformatie.getLayout();
            cl.next(tijdInformatie);
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
    }

    private void addBoat() {
        try{
            int newBoatID = Integer.parseInt(boatIdInputField.getText());
            if(!reservedBoats.contains(newBoatID)){
                boatIDs.add(newBoatID);
                addedBoatsLabel.setText("Added: " + boatIDs.toString());
                boatIdInputField.setText("");
                errorLabel.setText(" ");
            }else{
                errorLabel.setText("Die boot is al gereserveerd");
            }//TODO don't add if there is no boat with the given ID

        } catch (NumberFormatException ex) {
            //throw new RuntimeException(ex);
            errorLabel.setText("vul een getal in");
        }
    }

    private void removeBoat() {
        try{
            int boatID = Integer.parseInt(boatIdInputField.getText());
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
            dispose();
        }
    }

    private void addReservation() {
        ReservationRecord newReservation = getReservationRecord();

        reservationService.addReservation(newReservation);
        this.DisplayTab.reserveBoat(boatIDs);
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
                boatIDs
        );
    }

    private void onCancel() {
        // add your code here if necessary
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
