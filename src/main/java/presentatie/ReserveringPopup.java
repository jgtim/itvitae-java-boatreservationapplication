package presentatie;

import service.Reservation;

import javax.swing.*;
import java.awt.event.*;

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

    public ReserveringPopup() {
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
    }

    private void onOK() {
        Reservation reservering = new Reservation(
                nameField.getText(),
                Integer.parseInt(phoneNumberField.getText()),
                emailField.getText(),
                kamerFacturatieCheckBox.isSelected(),
                betaaldCheckBox.isSelected()
        );
        System.out.println(reservering);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ReserveringPopup dialog = new ReserveringPopup();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
