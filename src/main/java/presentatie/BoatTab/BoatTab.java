package presentatie.BoatTab;

import domain.BoatType;
import domain.LakeArea;
import domain.MolvenoColor;
import domain.MolvenoFont;
import service.Boat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

public class BoatTab extends JPanel {

    public BoatTab() {
        // Setting BoatTab layout as BorderLayout
        setLayout(new BorderLayout());

        // Creating ArrayList to work with Boat class data
        ArrayList<BoatType> boatTypeList = new ArrayList<>();

        // Creating List of all individual Boats | Testing search function of scrollable pane on left side
        ArrayList<Boat> boatList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            boatList.add(new Boat(BoatType.STANDARD_ROWBOAT, "Rowboat " + i, LakeArea.CENTRAL_LAKE, 2 ,false, true, 12.5, 22.5, i));
            boatList.add(new Boat(BoatType.FAMILY_ROWBOAT, "Rowboat " + i, LakeArea.CENTRAL_LAKE, 2 ,false, true, 12.5, 22.5, i));
            boatList.add(new Boat(BoatType.KAYAK, "Kayak " + i, LakeArea.CENTRAL_LAKE, 2 ,false, true, 12.5, 22.5, i));
            boatList.add(new Boat(BoatType.PEDAL, "Pedal Boat " + i, LakeArea.CENTRAL_LAKE, 2 ,false, true, 12.5, 22.5, i));
            boatList.add(new Boat(BoatType.SILENT_ELECTRIC, "Electric Boat " + i, LakeArea.CENTRAL_LAKE, 2 ,false, true, 12.5, 22.5, i));
            boatList.add(new Boat(BoatType.PREMIUM_ELECTRIC, "Electric Boat " + i, LakeArea.CENTRAL_LAKE, 2 ,false, true, 12.5, 22.5, i));
        }

        // RIGHT SIDE -------------------------------------------------------------------------------------------------
        // JPanel boat details (on right side of tab)
        JPanel boatDetails = new JPanel(new BorderLayout(10, 10));
        boatDetails.setBorder(new EmptyBorder(10, 10, 10, 10));
        boatDetails.setBackground(MolvenoColor.LIGHT.getColor());

        // Header Button (NORTH on JPanel boatDetails)
        JButton detailHeaderBtn = new JButton("Find and select your boat on the left hand side.");
        detailHeaderBtn.setContentAreaFilled(false);
        detailHeaderBtn.setBorderPainted(false);
        detailHeaderBtn.setFocusPainted(false);
        detailHeaderBtn.setFont(MolvenoFont.H3.getFont());
        detailHeaderBtn.setForeground(MolvenoColor.DARK.getColor());
        detailHeaderBtn.setBackground(MolvenoColor.BEIGE.getColor());
        // Adding Header Button to North side boatDetails JPanel
        boatDetails.add(detailHeaderBtn, BorderLayout.NORTH);

        // JPanel detailPanel, divided in LEFT and RIGHT, below detailHeaderBtn
        JPanel detailPanel = new JPanel(new GridLayout(1, 2));
        detailPanel.setBorder(BorderFactory.createLineBorder(MolvenoColor.DARK.getColor()));
        detailPanel.setBackground(MolvenoColor.LIGHT.getColor());

        // LEFT, 1st column of detailPanel; text in the form of JLabel detailLabel
        JLabel detailLabel = new JLabel("");
        detailLabel.setFont(MolvenoFont.PARAGRAPH.getFont());
        detailLabel.setForeground(MolvenoColor.DARK.getColor());
        detailLabel.setVerticalAlignment(SwingConstants.TOP);
        detailLabel.setHorizontalAlignment(SwingConstants.LEFT);
        detailLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        detailPanel.add(detailLabel);

        // RIGHT, 2nd column of detailPanel; photo placeholder in the form of JPanel photoPanel (can be removed later)
        // This photoPanel needs a GridBagLayout, to make sure the line and padding are implemented
        JPanel photoPanel = new JPanel(new GridBagLayout());
        photoPanel.setFont(MolvenoFont.PARAGRAPH.getFont());
        photoPanel.setForeground(MolvenoColor.DARK.getColor());
        photoPanel.setBackground(MolvenoColor.BLUE.getColor());



        photoPanel.setBorder(BorderFactory.createMatteBorder(50, 50, 50, 50, MolvenoColor.LIGHT.getColor()));

        JLabel photoLabel = new JLabel("PHOTO");
        photoPanel.add(photoLabel);
        detailPanel.add(photoPanel);

        // Add JPanel detailPanel (with on LEFT detailLabel and on RIGHT photoPanel) to JPanel boatDetails
        boatDetails.add(detailPanel, BorderLayout.CENTER);


        // --- SIDEBAR ON LEFT ----------------------------------------------------------------------------------------
        // Create JPanel with a CardLayout for swapping views:
        // From selecting Boat Types (top card) to the Boat Type members (bottom card)
        CardLayout cardLayout = new CardLayout();
        JPanel boatSidePanel = new JPanel(cardLayout);

        // Creating two panels: one for Boat Types and one for Boat Type Members
        // --- Boat Type Panel ---
        JPanel boatTypePanel = new JPanel();
        boatTypePanel.setLayout(new BoxLayout(boatTypePanel, BoxLayout.Y_AXIS));
        boatTypePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        boatTypePanel.setBackground(MolvenoColor.LIGHT.getColor());

        // --- Boat Type Member Panel ---
        JPanel boatTypeMemberPanel = new JPanel();
        boatTypeMemberPanel.setLayout(new BoxLayout(boatTypeMemberPanel, BoxLayout.Y_AXIS));
        boatTypeMemberPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        boatTypeMemberPanel.setBackground(MolvenoColor.LIGHT.getColor());

        // --- Adding Panel Cards to JPanel with CardLayout ---
        boatSidePanel.add(boatTypePanel, "BOAT_TYPE_SELECT");
        boatSidePanel.add(boatTypeMemberPanel, "BOAT_MEMBER_SELECT");

        // Calling method to create JButtons in BoatTypePanel and implements search function for Boat Types
        // This method calls another method: one that creates JButtons for Boat Type Members
        populateBoatTypeButtons(boatTypePanel, cardLayout, boatSidePanel, boatTypeMemberPanel, detailHeaderBtn, detailLabel, boatList);

        // Here we put the created and functional boatTypePanel with CardLayout in a scrollable JScrollPane
        JScrollPane boatScrollPane = new JScrollPane(boatSidePanel);
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


        // ------------------------------------------------------------------------------------------------------------
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
                Component[] components = boatTypePanel.getComponents();

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
                boatTypePanel.revalidate();
                boatTypePanel.repaint();
            }
        });
        // ------------------------------------------------------------------------------------------------------------

        // Create JPanel wrapper for boatSearchField so margins can be added
        JPanel boatSearchBarWrapper = new JPanel(new BorderLayout());
        boatSearchBarWrapper.setBackground(MolvenoColor.LIGHT.getColor());
        boatSearchBarWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        boatSearchBarWrapper.add(boatSearchField, BorderLayout.CENTER);

        // Create JPanel wrapper for combining boatSearchBarWrapper (NORTH) and boatScrollPane (CENTER)
        JPanel sidebarWrapper = new JPanel(new BorderLayout());
        sidebarWrapper.setPreferredSize(new Dimension(250, 0));
        sidebarWrapper.add(boatSearchBarWrapper, BorderLayout.NORTH);
        sidebarWrapper.add(boatScrollPane, BorderLayout.CENTER);

        add(sidebarWrapper, BorderLayout.WEST);
        add(boatDetails, BorderLayout.CENTER);
    }


    // METHODS ---------------------------------------------------------------------------------------------------------
    // Method for populating Boat Type Buttons in the left side Scroll Pane (top card)
    private void populateBoatTypeButtons(JPanel targetPanel, CardLayout cardLayout, JPanel deck, JPanel memberPanel, JButton header, JLabel details, ArrayList<Boat> fullBoatList) {
        targetPanel.removeAll();

        for (BoatType type : BoatType.values()) {
            JButton typeBtn = new JButton(type.getDisplayBoatType());
            typeBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            typeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            typeBtn.setFont(MolvenoFont.BUTTONLABEL.getFont());
            typeBtn.setForeground(MolvenoColor.DARK.getColor());
            typeBtn.setBackground(MolvenoColor.BEIGE.getColor());

            // When we click on a type:
            typeBtn.addActionListener(e -> {
                // Filter a list of boats of ONLY the type clicked
                ArrayList<Boat> filteredBoats = new ArrayList<>();
                for (Boat b : fullBoatList) {
                    if (b.getBoatType().equals(type)) {
                        filteredBoats.add(b);
                    }
                }
                // Populate Member card with that list:
                populateBoatMemberButtons(filteredBoats, memberPanel, header, details, cardLayout, deck);

                // Go to the Member card
                cardLayout.show(deck, "BOAT_MEMBER_SELECT");
            });
            targetPanel.add(typeBtn);
            targetPanel.add(Box.createVerticalStrut(5));
        }
        targetPanel.revalidate();
        targetPanel.repaint();
    }


    // Method for populating Boat Member Buttons in the left side Scroll Pane (bottom card)
    private void populateBoatMemberButtons(ArrayList<Boat> boatList, JPanel targetPanel, JButton header, JLabel details, CardLayout cardLayout, JPanel deck) {
        targetPanel.removeAll();

        // Creating single button on top to return to Boat Types
        JButton typesButton = new JButton("<<< Go Back");
        typesButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        typesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        typesButton.setFont(MolvenoFont.BUTTONLABEL.getFont());
        typesButton.setForeground(MolvenoColor.DARK.getColor());
        typesButton.setBackground(MolvenoColor.BEIGE.getColor());

        typesButton.addActionListener(e -> cardLayout.show(deck, "BOAT_TYPE_SELECT"));

        targetPanel.add(typesButton);
        targetPanel.add(Box.createVerticalStrut(15)); // Extra space after back button

        for (Boat b : boatList) {
            // for every boat in list of boats, create and style a JButton
            JButton boatBtn = new JButton(b.getBoatTypeMember());
            boatBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            boatBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            boatBtn.setFont(MolvenoFont.BUTTONLABEL.getFont());
            boatBtn.setForeground(MolvenoColor.DARK.getColor());
            boatBtn.setBackground(MolvenoColor.BEIGE.getColor());

            // Creating interaction between Boat Buttons in JScrollPane and JButton detailHeader
            boatBtn.addActionListener(e -> {
                header.setText(b.getBoatTypeMember());
                // Creating String boatInfo in html formatting to display Boat object boatInfo
                String boatInfo = "<html>" +
                        "<b>Type:</b> " + b.getBoatType().getDisplayBoatType() + "<br>" +
                        "<b>Name:</b> " + b.getBoatTypeMember() + "<br><br>" +
                        "<b>Lake Area:</b> " + b.getLakeArea().getDisplayLakeArea() + "<br><br>" +

                        "<b>Capacity:</b> " + b.getCapacity() + "<br><br>" +

                        "<b>Electric motor:</b> " + (b.isElectric() ? "Yes" : "No") + "<br>" +
                        "<b>Life jacket on board:</b> " + (b.hasLifeJacket() ? "Yes" : "No") + "<br><br>" +

                        "<b>Price per 15 minutes:</b> " + b.getPrice15() + " euros <br>" +
                        "<b>Price per 30 minutes:</b> " + b.getPrice30() + " euros <br><br>" +

                        "<b>Boot ID:</b> " + b.getId() +
                        "<html>";
                // Setting html formatted String boatInfo to JLabel detailLabel
                details.setText(boatInfo);
            });

            // Adding functional Boat Buttons to JPanel boatTypePanel
            targetPanel.add(boatBtn);
            targetPanel.add(Box.createVerticalStrut(5)); // Because we're using BoxLayout, we need vertical strut to create margins
        }
        targetPanel.revalidate();
        targetPanel.repaint();
    }
}