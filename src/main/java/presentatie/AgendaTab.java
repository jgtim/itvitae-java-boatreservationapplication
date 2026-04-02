package presentatie;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;

public class AgendaTab extends JPanel {
    private JButton newReservationButton;
    private JPanel panel1;
    private DatePicker datePicker;

    public AgendaTab() {
        setLayout(new GridBagLayout());
        add(panel1);
    }
}
