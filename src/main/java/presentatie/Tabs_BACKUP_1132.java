package presentatie;

import data.ReservationJsonHandler;
import data.ReservationRepositoryHandler;
import domain.MolvenoFont;
import domain.TabIndex;
import presentatie.BoatTab.BoatTab;
import presentatie.Calendar.Calendar;
import presentatie.Calendar.CalendarDisplay;


import javax.swing.*;

public class Tabs extends JFrame {

    Tabs() {
        // UIManager Class manipulation

        // --- Setting Look and Feel:
        // Default Java: getCrossPlatformLookAndFeelClassName()
        // System Native: UIManager.getSystemLookAndFeelClassName
        // Vector Based: "javax.swing.plaf.nimbus.NimbusLookAndFeel"
        try {
            UIManager.setLookAndFeel((UIManager.getSystemLookAndFeelClassName()));
        } catch  (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Changing font of the TabbedPane (not possible without UIManager)
        UIManager.put("TabbedPane.font", MolvenoFont.H3.getFont());

        // Configuring MainFrame (window voor window voor tabbladen)
        setTitle("Molveno Boat Rental");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);

        // Setting Window Icon
        ImageIcon logo = new ImageIcon(Tabs.class.getResource("/images/MolvenoLogo.png"));
        setIconImage(logo.getImage());

        // Creating JTabbedPane (window voor tabbladen)
        JTabbedPane tabPane = new JTabbedPane();

        // Creating JPanels (tabbladen)
<<<<<<< HEAD
        JPanel boatCatalogue = new BoatTab();
        JPanel agenda = new AgendaTab();
        JPanel reservations = new ReservationsTab();
=======
        JPanel bootCatalogue = new JPanel();
        bootCatalogue.add(new JLabel("Welkom bij de Boot Catalogus"));

        ReservationRepositoryHandler reservationRepositoryHandler = new ReservationJsonHandler();
        JPanel agenda = new CalendarDisplay(reservationRepositoryHandler);
        ReservationsTab reservations = new ReservationsTab(reservationRepositoryHandler);
>>>>>>> main

        // Adding JPanels to JTabbedPane

        tabPane.add("Agenda", agenda);
        tabPane.add("Catalogue", boatCatalogue);
        tabPane.add("Reservations", reservations);

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
