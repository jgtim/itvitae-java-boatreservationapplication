package presentatie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AgendaTab extends JPanel {
    private JButton newReservationButton;
    private JPanel panel1;

    public AgendaTab() {
        setLayout(new GridBagLayout());
        add(panel1);
        newReservationButton.addActionListener(e -> {
            ReserveringPopup reserveringPopup = new ReserveringPopup();
            reserveringPopup.pack();
            reserveringPopup.setLocationRelativeTo(null);
            reserveringPopup.setVisible(true);
        });
    }
}
