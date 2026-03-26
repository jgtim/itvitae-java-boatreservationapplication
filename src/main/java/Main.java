import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        ImageIcon logo = new ImageIcon(Main.class.getResource("/images/MolvenoLogo.png"));

        // Creating MainFrame and JTabbedPane for the application
        JFrame app = new JFrame("Molveno Bootverhuur");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(1280, 720);
        app.setFont(Fonts.H1);
        app.setForeground(Colors.DARK);
        app.setBackground(Colors.LIGHT);
        app.setIconImage(logo.getImage());
        JTabbedPane tabPane = new JTabbedPane();

        // Creating Panels (tabbladen) for the application
        JPanel bootCatalogue = new JPanel();
        bootCatalogue.add(new JLabel("Welkom bij de Boot Catalogus"));

        JPanel agenda = new JPanel();
        agenda.add(new JLabel("Welkom bij de Agenda"));

        JPanel reservations = new JPanel();
        reservations.add(new JLabel("Welkom bij de Reserveringen"));

        // Adding Panels (tabbladen) to the JTabbedPane
        tabPane.add("Agenda", agenda);
        tabPane.add("Catalogus", bootCatalogue);
        tabPane.add("Reserveringen", reservations);

        app.add(tabPane);
        app.setVisible(true);
    }
}
