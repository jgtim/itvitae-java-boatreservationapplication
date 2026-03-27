package domain;

import java.awt.Font;
import java.io.InputStream;

public enum MolvenoFont {
    H1("Ubuntu-Bold.ttf", 32f, Font.BOLD),
    H2("Ubuntu-Bold.ttf", 24f, Font.BOLD),
    H3("Ubuntu-Bold.ttf", 18f, Font.BOLD),
    PARAGRAPH("Ubuntu-Regular.ttf", 16f, Font.PLAIN),
    BUTTONLABEL("Ubuntu-Medium.ttf", 16f, Font.PLAIN);

    private final Font font;

    MolvenoFont(String path, float size, int style) {
        this.font = loadFont(path, size, style);
    }


    public Font getFont() {
        return font;
    }

    private static Font loadFont(String path, float size, int style) {
        try (InputStream is = MolvenoFont.class.getResourceAsStream("/fonts/" + path)) {
            if  (is == null) {
                throw new Exception("Font not found");
            }
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
        } catch (Exception e) {
            System.err.println("Error loading font, using default SERIF.");
            return new Font(Font.SERIF, style, (int)size);
        }
    }
}
