import javax.swing.*;

public class Tabs extends JFrame {

    Tabs() {
        // Configuring MainFrame (window voor window voor tabbladen)
        setTitle("Molveno Bootverhuur");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setFont(Fonts.H1);
        setForeground(Colors.DARK);
        setBackground(Colors.LIGHT);

        // Setting Window Icon
        ImageIcon logo = new ImageIcon(Main.class.getResource("/images/MolvenoLogo.png"));
        setIconImage(logo.getImage());

        // Creating JTabbedPane (window voor tabbladen)
        JTabbedPane tabPane = new JTabbedPane();

        // Creating JPanels (tabbladen)
        JPanel bootCatalogue = new JPanel();
        bootCatalogue.add(new JLabel("Welkom bij de Boot Catalogus"));

        JPanel agenda = new JPanel();
        agenda.add(new JLabel("Welkom bij de Agenda"));

        JPanel reservations = new JPanel();
        reservations.add(new JLabel("Welkom bij de Reserveringen"));

        // Adding JPanels to JTabbedPane
        tabPane.add("Agenda", agenda);
        tabPane.add("Catalogus", bootCatalogue);
        tabPane.add("Reserveringen", reservations);

        // Adding JTabbedPane to JFrame and setting to visible
        add(tabPane);
        setVisible(true);
    }
}
