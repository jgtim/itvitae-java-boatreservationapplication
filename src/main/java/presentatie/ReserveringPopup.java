package presentatie;

import data.BoatDataExcel;
import domain.BoatRecord;
import domain.ReservationRecord;
import presentatie.Calendar.CalendarDisplay;
import service.ReservationDto;
import service.ReservationService.ReservationService;

import javax.swing.*;
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
    private JLabel priceField;
    private LocalDate datum;
    private String begintijd;
    private String eindtijd;
    private ArrayList<Integer> boatIDs = new ArrayList<Integer>();
    private ReservationService reservationService;
    private ArrayList<Integer> reservedBoats;
    private double price = 0.00;
    private int times30;
    private boolean add15;
    private List<Integer> validIDs;
    private ArrayList<BoatRecord> allBoats;

    public ReserveringPopup(ReservationService reservationService, CalendarDisplay display, String van, String tot, LocalDate datum, ArrayList<Integer> reservedBoats) { //LocalDate date
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
            System.out.println("ODD");
        }
        else{
            System.out.println("EVEN");
            this.add15 = false;
        }
        System.out.println("TIJDSDUUR: " + tijdsduur);
        times30 = tijdsduur / 2;
        dateField.setText(datum.toString());
        var boatDataHandler = new BoatDataExcel();
        ArrayList<BoatRecord> allBoats = boatDataHandler.read();

        this.validIDs = Arrays.asList(
                allBoats.stream()
                        .map(BoatRecord::id)
                        .toArray(Integer[]::new)
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
        addBoatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int newBoatID = Integer.parseInt(boatIdInputField.getText());
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

            }
        });
    }

    // Constructor voor gebruik in reserveringen tab om een reservering in te zien of te wijzigen
    public ReserveringPopup(ReservationDto reservationDto) {
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

        ReservationRecord reservationRecord = reservationDto.getReservationRecord();

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

        buttonOK.setText("Wijzig");
        buttonOK.addActionListener(e -> setEditMode());

        buttonCancel.addActionListener(e -> onCancel());
        this.price = reservationRecord.prijs();
        priceField.setText("€" + String.format("%.2f", price));


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void setEditMode() {
        buttonOK.setText("Bevestig");
        comboBoxVan.setSelectedItem(vanLabel.getText());
        comboBoxTot.setSelectedItem(totLabel.getText());
        CardLayout cl = (CardLayout) tijdInformatie.getLayout();
        cl.next(tijdInformatie);
    }

    private void onOK() {
        if (!isFormValid()) {
            return;
        }

        ReservationDto newReservation = getReservationDto();

        reservationService.addReservation(newReservation);
        this.DisplayTab.reserveBoat(boatIDs);
        dispose();
    }

    private ReservationDto getReservationDto() {
        ReservationDto newReservation = new ReservationDto(
                reservationService.getNewReserveringsNummer(),
                nameField.getText(),
                (phoneNumberField.getText().isEmpty()) ? null : Integer.parseInt(phoneNumberField.getText().replace(" ", "")),
                (emailField.getText().isEmpty()) ? null : emailField.getText(),
                kamerFacturatieCheckBox.isSelected(),
                betaaldCheckBox.isSelected(),
                datum.toString(),
                begintijd,
                eindtijd,
                boatIDs,
                price

        );


        return newReservation;
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
