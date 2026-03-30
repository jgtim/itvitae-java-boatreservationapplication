import domain.MolvenoFont;
import presentatie.AgendaTab;

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

        JPanel agenda = new AgendaTab();
//        agenda.add(new JLabel("Welkom bij de Agenda"));


        JPanel reservations = new JPanel();
        reservations.add(new JLabel("Welkom bij de Reserveringen"));

        // Adding JPanels to JTabbedPane

        tabPane.add("Agenda", agenda);
        tabPane.add("Catalogus", new BoatTab());
        tabPane.add("Reserveringen", reservations);

        // Adding JTabbedPane to JFrame and setting to visible
        add(tabPane);
        setVisible(true);
    }
}
