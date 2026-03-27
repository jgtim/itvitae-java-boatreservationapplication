package domain;

public record CustomerRecord(String naam, Integer telefoonNummer, String email) {

    public CustomerRecord(String naam, int telefoonNummer) {
        this(naam, telefoonNummer, "N/A");
    }

    public CustomerRecord(String naam, String email) {
        this(naam, 0, email);
    }

    @Override
    public String toString() {
        return naam + ", " + telefoonNummer + ", " + email;
    }
}
