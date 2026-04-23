package presentatie;

import domain.MolvenoFont;
import domain.TabIndex;
import presentatie.BoatTab.BoatTab;
import presentatie.Calendar.CalendarDisplay;
import service.BoatService.BoatService;
import service.ReservationService.ReservationService;

import javax.swing.*;

public class App extends JFrame {

    App(ReservationService reservationService, BoatService boatService) {
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

        // Changing font ob the TabbedPane (not possible without UIManager)
        UIManager.put("TabbedPane.font", MolvenoFont.H3.getFont());

        // Configuring MainFrame (window voor window voor tabbladen)
        setTitle("Molveno Bootverhuur");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);

        // Setting Window Icon
        ImageIcon logo = new ImageIcon(App.class.getResource("/images/MolvenoLogo.png"));
        setIconImage(logo.getImage());

        // Creating JTabbedPane (window voor tabbladen)
        JTabbedPane tabPane = new JTabbedPane();

        // Creating JPanels (tabbladen)
        JPanel agenda = new CalendarDisplay(reservationService, boatService);
        JPanel bootCatalogue = new BoatTab(boatService);
        ReservationsTab reservations = new ReservationsTab(reservationService);

        // Adding JPanels to JTabbedPane
        tabPane.add("Agenda", agenda);
        tabPane.add("Catalogus", bootCatalogue);
        tabPane.add("Reserveringen", reservations);

        // Add a listener for when the tab changes
        tabPane.addChangeListener(e -> {
            int selectedIndex = tabPane.getSelectedIndex();

            if (selectedIndex == TabIndex.RESERVERINGEN.getIndex()) {
                reservations.onSelected();
            } else if (selectedIndex == TabIndex.AGENDA.getIndex()) {
                ((CalendarDisplay) agenda).onSelected(reservationService, boatService);
            }
        });

        // Adding JTabbedPane to JFrame and setting to visible
        add(tabPane);
        setVisible(true);
    }
}
