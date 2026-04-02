import data.ReservationJsonHandler;
import data.ReservationRepositoryHandler;
import domain.MolvenoFont;
import domain.TabIndex;
import presentatie.AgendaTab;
import presentatie.ReservationsTab;

import javax.swing.*;

public class Tabs extends JFrame {

    Tabs() {
        UIManager.put("TabbedPane.font", MolvenoFont.H3.getFont());

        // Configuring MainFrame (window voor window voor tabbladen)
        setTitle("Molveno Bootverhuur");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);

        // Setting Window Icon
        ImageIcon logo = new ImageIcon(Tabs.class.getResource("/images/MolvenoLogo.png"));
        setIconImage(logo.getImage());

        // Creating JTabbedPane (window voor tabbladen)
        JTabbedPane tabPane = new JTabbedPane();

        // Creating JPanels (tabbladen)
        JPanel bootCatalogue = new JPanel();
        bootCatalogue.add(new JLabel("Welkom bij de Boot Catalogus"));


        ReservationRepositoryHandler reservationRepositoryHandler = new ReservationJsonHandler();
        ReservationsTab reservations = new ReservationsTab(reservationRepositoryHandler);
        AgendaTab agenda = new AgendaTab(reservationRepositoryHandler);

        // Adding JPanels to JTabbedPane

        tabPane.add("Agenda", agenda);
        tabPane.add("Catalogus", new BoatTab());
        tabPane.add("Reserveringen", reservations);

        // Add a listener for when the tab changes
        tabPane.addChangeListener(e -> {
            int selectedIndex = tabPane.getSelectedIndex();
            if (selectedIndex == TabIndex.RESERVERINGEN.getIndex()) {
                reservations.onSelected();
            }
        });

        // Adding JTabbedPane to JFrame and setting to visible
        add(tabPane);
        setVisible(true);
    }
}
