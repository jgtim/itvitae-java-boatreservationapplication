package presentatie;

import data.ReservationRepositoryHandler;
import presentatie.Calendar.CalendarDisplay;
import service.ReservationDto;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReserveringPopup extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel inputPane;
    private JTextField nameField;
    private JTextField phoneNumberField;
    private JCheckBox kamerFacturatieCheckBox;
    private JPanel tijdInformatie;
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
    private LocalDate datum;
    private String begintijd;
    private String eindtijd;
    private ArrayList<Integer> boatIDs = new ArrayList<Integer>();
    private ReservationRepositoryHandler reservationRepositoryHandler;

    public ReserveringPopup(ReservationRepositoryHandler handler, CalendarDisplay display, String van, String tot, LocalDate datum) { //LocalDate date
        this.DisplayTab = display;
        this.begintijd = van;
        this.eindtijd = tot;
        this.datum = datum;
        vanLabel.setText(van);
        totLabel.setText(tot);
        dateField.setText(datum.toString());
        reservationRepositoryHandler = handler;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

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
                int newBoatID = Integer.parseInt(boatIdInputField.getText());
                boatIDs.add(newBoatID);
                addedBoatsLabel.setText("Added: " + boatIDs.toString());
                boatIdInputField.setText("");
            }
        });
    }

    private void onOK() {
        if (!isFormValid()) {
            return;
        }

        ReservationDto newReservation = getReservationDto();

        reservationRepositoryHandler.addReservation(newReservation);
        this.DisplayTab.reserveBoat();
        dispose();
    }

    private ReservationDto getReservationDto() {
        ReservationDto newReservation = new ReservationDto(
                nameField.getText(),
                (phoneNumberField.getText().isEmpty()) ? null : Integer.parseInt(phoneNumberField.getText().replace(" ", "")),
                (emailField.getText().isEmpty()) ? null : emailField.getText(),
                kamerFacturatieCheckBox.isSelected(),
                betaaldCheckBox.isSelected(),
                datum.toString(),
                begintijd,
                eindtijd,
                boatIDs

        );


        return newReservation;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private boolean isFormValid() {
        if (nameField.getText().isEmpty()) {
            System.out.println("Must enter name");
            return false;
        } else if (phoneNumberField.getText().isEmpty() && emailField.getText().isEmpty()) {
            System.out.println("Must enter phone number or email");
            return false;
        }
        return true;
    }
}
