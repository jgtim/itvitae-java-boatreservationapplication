package domain;
import java.awt.*;

public enum MolvenoColor {
    DARK(0x282b2c), // primary text
    LIGHT(0xffffff), // primary background
    BEIGE(0xd8d0c5), // secondary background
    BLUE(0x618b98), // water
    GREEN(0x77a368), // nature
    ORANGE(0xdf7d51); // accent color for highlights/actions

    private final Color color;

    MolvenoColor(int hex) {
        this.color = new Color(hex);
    }

    public Color getColor() {
        return color;
    }
}
