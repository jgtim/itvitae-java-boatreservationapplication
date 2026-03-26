import java.awt.*;
import java.io.File;
import java.io.InputStream;

// Public Class Fonts that contains all fonts requested by Molveno Boat Rental, with names and uses
public class Fonts {
    // Can't seem to make them final because of Catch block below
    public static Font H1;
    public static Font H2;
    public static Font H3;
    public static Font PARAGRAPH;
    public static Font BUTTONLABEL;

    // Static try and catch block to import font files and assign to Font fields
    // Probably need to find a better/more secure and adaptable way later
    static {
        try {
            InputStream loadingHeader = Fonts.class.getResourceAsStream("fonts/Ubuntu-Bold.ttf");
            InputStream loadingParagraph = Fonts.class.getResourceAsStream("fonts/Ubuntu-Regular.ttf");
            InputStream loadingButtonLabel = Fonts.class.getResourceAsStream("fonts/Ubuntu-Medium.ttf");



            Font basicHeader = Font.createFont(Font.TRUETYPE_FONT, loadingHeader);
            Font basicParagraph = Font.createFont(Font.TRUETYPE_FONT, loadingParagraph);
            Font basicButtonLabel = Font.createFont(Font.TRUETYPE_FONT, loadingButtonLabel);

            // Setting fond size. Need to use float values
            H1 = basicHeader.deriveFont(32f);
            H2 = basicHeader.deriveFont(24f);
            H3 = basicHeader.deriveFont(18f);
            PARAGRAPH = basicParagraph.deriveFont(16f);
            BUTTONLABEL = basicButtonLabel.deriveFont(16f);

            // In case shit goes wrong
        } catch (Exception e) {
            System.err.println("Font not found, using default SERIF.");
            H1 = new Font(Font.SERIF, Font.BOLD, 32);
            H2 = new Font(Font.SERIF, Font.BOLD, 24);
            H3 = new Font(Font.SERIF, Font.BOLD, 18);
            PARAGRAPH = new Font(Font.SERIF, Font.PLAIN, 16);
            BUTTONLABEL = new Font(Font.SERIF, Font.PLAIN, 16);
        }
    }
}
