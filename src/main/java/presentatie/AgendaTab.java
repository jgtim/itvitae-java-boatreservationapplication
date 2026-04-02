package presentatie;

import com.github.lgooddatepicker.components.DatePicker;
import presentatie.Calendar.Calendar;

import javax.swing.*;
import java.awt.*;

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
//            ReserveringPopup reserveringPopup = new ReserveringPopup(datePicker.getDate());
//            reserveringPopup.pack();
//            reserveringPopup.setLocationRelativeTo(null);
//            reserveringPopup.setVisible(true);
        });
    }
}
