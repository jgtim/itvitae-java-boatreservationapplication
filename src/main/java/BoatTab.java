import domain.MolvenoColor;
import domain.MolvenoFont;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

public class BoatTab extends JPanel {
    // Begonnen aan het versimpelen van de code. Misschien later afmaken
    //    private final JPanel boatSidePanel = new JPanel();
    //    private final JTextField boatSearchField = new JTextField();
    //    private final JLabel detailLabel = new JLabel("");
    //    private final JButton detailHeaderBtn = new JButton("Zoek en selecteer een boot.");

    BoatTab() {
        // Setting layout as BorderLayout
        setLayout(new BorderLayout());

        // Creating ArrayList to work with Boat class data
        ArrayList<Boat> boatList = new ArrayList<>();
        for (int i = 1; i <= 60; i++) {
            boatList.add(new Boat("Boot " + i, i, 15.90, 27.50, "Elektrische Sloep", "Koelkast, zonnescherm"));
        }

        // Boat details (on right side)
        JPanel boatDetails = new JPanel(new BorderLayout(10, 10));
        boatDetails.setBorder(new EmptyBorder(15, 15, 15, 15));
        boatDetails.setBackground(MolvenoColor.LIGHT.getColor());

        // Header Button (NORTH on JPanel boatDetails)
        JButton detailHeaderBtn = new JButton("Zoek en selecteer een boot aan de linkerkant.");
        detailHeaderBtn.setContentAreaFilled(false);
        detailHeaderBtn.setBorderPainted(false);
        detailHeaderBtn.setFocusPainted(false);
        detailHeaderBtn.setFont(MolvenoFont.H3.getFont());
        detailHeaderBtn.setForeground(MolvenoColor.DARK.getColor());
        detailHeaderBtn.setBackground(MolvenoColor.BEIGE.getColor());
        boatDetails.add(detailHeaderBtn, BorderLayout.NORTH);

        // JPanel detailPanel, divided in LEFT and RIGHT, below detailHeaderBtn
        JPanel detailPanel = new JPanel(new GridLayout(1, 2));
        detailPanel.setBorder(BorderFactory.createLineBorder(MolvenoColor.DARK.getColor()));

        // LEFT; text in the form of JLabel detailLabel
        JLabel detailLabel = new JLabel("");
        detailLabel.setFont(MolvenoFont.PARAGRAPH.getFont());
        detailLabel.setForeground(MolvenoColor.DARK.getColor());
        detailLabel.setVerticalAlignment(SwingConstants.TOP);
        detailLabel.setHorizontalAlignment(SwingConstants.LEFT);
        detailLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        detailPanel.add(detailLabel);

        // RIGHT, photo placeholder in the form of JPanel photoPanel (can be removed later)
        JPanel photoPanel = new JPanel();
        photoPanel.setFont(MolvenoFont.PARAGRAPH.getFont());
        photoPanel.setForeground(MolvenoColor.DARK.getColor());
        photoPanel.setBackground(MolvenoColor.BLUE.getColor());
        photoPanel.setBorder(BorderFactory.createLineBorder(MolvenoColor.DARK.getColor()));
        photoPanel.add(new JLabel("FOTO"));
        detailPanel.add(photoPanel);

        // Add JPanel detailPanel (with on LEFT detailLabel and on RIGHT photoPanel) to JPanel boatDetails
        boatDetails.add(detailPanel, BorderLayout.CENTER);


        // --- SIDEBAR ON LEFT ---
        // create JPanel boatSidePanel with infinite amount of rows (syntax 0), 1 column
        JPanel boatSidePanel = new JPanel();
        boatSidePanel.setLayout(new BoxLayout(boatSidePanel, BoxLayout.Y_AXIS));
        boatSidePanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        boatSidePanel.setBackground(MolvenoColor.LIGHT.getColor());

        // Looping over ArrayList boatList and creating button for each element, setting button dimensions. Then adding to ArrayList boatButtons
        ArrayList<JButton> boatButtons = new ArrayList<>();

        for (Boat b : boatList) {
            JButton boatBtn = new JButton(b.getName());
            boatBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            boatBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            boatBtn.setFont(MolvenoFont.BUTTONLABEL.getFont());
            boatBtn.setForeground(MolvenoColor.DARK.getColor());
            boatBtn.setBackground(MolvenoColor.BEIGE.getColor());
            boatButtons.add(boatBtn);

            // Creating interaction between Boat Buttons in JScrollPane and JButton detailHeader
            boatBtn.addActionListener(e -> {
                detailHeaderBtn.setText(b.getName());

                // Creating String boatInfo in html formatting to display Boat object boatInfo
                String boatInfo = "<html>" +
                        "<b>Naam:</b> " + b.getName() + "<br><br>" +
                        "<b>Capaciteit:</b> " + b.getCapacity() + "<br><br>" +
                        "<b>Prijs per 15 minuten:</b> " + b.getPrice15() + "<br>" +
                        "<b>Prijs per 30 minuten:</b> " + b.getPrice30() + "<br><br>" +
                        "<b>Type:</b> " + b.getType() + "<br><br>" +
                        "<b>Kenmerken:</b> " + b.getFeatures() + "<br><br>" +
                        "<b>Boot ID:</b> " + b.hashCode() +
                        "<html>";
                // Setting html formatted String boatInfo to JLabel detailLabel
                detailLabel.setText(boatInfo);
            });

            // Adding functional Boat Buttons to JPanel boatSidePanel
            boatSidePanel.add(boatBtn);
            boatSidePanel.add(Box.createVerticalStrut(5)); // Because we're using BoxLayout, we need vertical strut to create margins
        }

        // Put JPanel boatSidePanel in a scrollable JScrollPane boatScrollPane
        JScrollPane boatScrollPane = new JScrollPane(boatSidePanel);
        // set scrollPane behavior
        boatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // just making sure
        boatScrollPane.getVerticalScrollBar().setUnitIncrement(16); // smoother scrolling, otherwise too slow
        boatScrollPane.setPreferredSize(new Dimension(180, 0)); // set Thumb and Track width


        // --- DIFFICULT SEARCH MAGIC BELOW ---
        // Creating a searchbar in the form of JTextField boatSearchField
        JTextField boatSearchField = new JTextField();

        // Difficult way of adjusting look and feel of boatSearchField to make it pretty
        Font searchTitleFont = MolvenoFont.PARAGRAPH.getFont();
        TitledBorder searchBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MolvenoColor.DARK.getColor()),
                "Zoek gewenste boot: ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                searchTitleFont,
                MolvenoColor.DARK.getColor()
        );
        // Easy adjusting look and feel
        boatSearchField.setBorder(searchBorder);
        boatSearchField.setPreferredSize(new Dimension(200, 60));
        boatSearchField.setFont(MolvenoFont.PARAGRAPH.getFont());
        boatSearchField.setForeground(MolvenoColor.DARK.getColor());


        // --- THE SEARCH LOGIC - GIFTED TO ME BY DARK FORCES - NEED TO UNDERSTAND LATER ---
        boatSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void filter() {
                // Creating String filter with is set to whatever text is typed in the boatSearchField (and changed to all lowercase)
                String filter = boatSearchField.getText().toLowerCase();
                // Creating Component Array to include the JButtons and created strut (for margins). Need to hide both for pretty view!
                Component[] components = boatSidePanel.getComponents();

                for (int i = 0; i < components.length; i++) {
                    Component comp = components[i];

                    if (comp instanceof JButton) {
                        JButton btn = (JButton) comp;
                        boolean matches = btn.getText().toLowerCase().contains(filter);
                        btn.setVisible(matches);

                        // Look at the NEXT component (the strut) and match its visibility to the button
                        if (i + 1 < components.length && !(components[i + 1] instanceof JButton)) {
                            components[i + 1].setVisible(matches);
                        }
                    }
                }
                boatSidePanel.revalidate();
                boatSidePanel.repaint();
            }
        });

        // Create JPanel wrapper for boatSearchField so margins can be added
        JPanel boatSearchBarWrapper = new JPanel(new BorderLayout());
        boatSearchBarWrapper.setBackground(MolvenoColor.LIGHT.getColor());
        boatSearchBarWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        boatSearchBarWrapper.add(boatSearchField, BorderLayout.CENTER);

        // Create JPanel wrapper for boatSearchBarWrapper (NORTH) and boatScrollPane (CENTER)
        JPanel sidebarWrapper = new JPanel(new BorderLayout());
        sidebarWrapper.setPreferredSize(new Dimension(200, 0));
        sidebarWrapper.add(boatSearchBarWrapper, BorderLayout.NORTH);
        sidebarWrapper.add(boatScrollPane, BorderLayout.CENTER);

        add(sidebarWrapper, BorderLayout.WEST);
        add(boatDetails, BorderLayout.CENTER);
    }
}