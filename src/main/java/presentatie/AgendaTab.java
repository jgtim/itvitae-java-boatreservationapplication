package presentatie;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class AgendaTab extends JPanel {
    private JButton newReservationButton;
    private JPanel panel1;
    private DatePicker datePicker;

    public AgendaTab() {
        setLayout(new GridBagLayout());
        add(panel1);
        datePicker.setDateToToday();
        newReservationButton.addActionListener(e -> {
            datePicker.setTextFieldToValidStateIfNeeded();
            ReserveringPopup reserveringPopup = new ReserveringPopup();
            reserveringPopup.setDate(datePicker.getDate());
            reserveringPopup.pack();
            reserveringPopup.setLocationRelativeTo(null);
            reserveringPopup.setVisible(true);
        });
    }
}
