package domain;

import java.util.regex.Pattern;

public record CustomerRecord(String naam, String telefoonNummer, String email) {

    public CustomerRecord(String naam, String telefoonOfEmail) {
        this(naam, isPhoneNumber(telefoonOfEmail) ? telefoonOfEmail : "", isPhoneNumber(telefoonOfEmail) ? "N/A" : telefoonOfEmail);
    }

    @Override
    public String toString() {
        return naam + ", " + telefoonNummer + ", " + email;
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        String pattern = "^(\\+\\d{1,3})?\\d{6,11}$";
        return phoneNumber.matches(pattern);
    }
}