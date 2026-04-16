package presentatie;

import domain.ReservationRecord;
import service.ReservationDto;

import javax.swing.*;
import java.awt.*;

public class ReservationItemPanel extends JPanel {

    private JPanel reservationItemContainer;
    private JPanel reservationItemPanel;
    private JButton terugButton;
    private JPanel header;
    private JPanel klantInfoPanel;
    private JPanel tijdInfoPanel;
    private JLabel naamLabel;
    private JLabel telefoonnummerLabel;
    private JLabel emailLabel;
    private JPanel cardLayoutParent;

    public ReservationItemPanel() {
        terugButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardLayoutParent.getLayout();
            cl.next(cardLayoutParent);
        });
    }

    public void setCardLayoutParent(JPanel parent) {
        cardLayoutParent = parent;
    }

    void changeReservation(ReservationDto reservationDto) {
        ReservationRecord reservationRecord =  reservationDto.getReservationRecord();
        naamLabel.setText(reservationRecord.customerRecord().naam());
        emailLabel.setText(reservationRecord.customerRecord().email());
        telefoonnummerLabel.setText(reservationRecord.customerRecord().email());
    }


}
