package domain;

public record HotelGuestRecord(String achternaam, String voornaam, Integer kamernummer, Integer telefoonnummer, String email) {

    public HotelGuestRecord(String achternaam, String voornaam, Integer kamernummer, Integer telefoonnummer) {
        this(achternaam, voornaam, kamernummer, telefoonnummer, "N/A");
    }

    public HotelGuestRecord(String achternaam, String voornaam, Integer kamernummer, String email) {
        this(achternaam, voornaam, kamernummer, 0, email);
    }

    @Override
    public String toString() {
        return achternaam + ", " + voornaam + ", " + kamernummer + ", " + telefoonnummer + ", " + email;
    }

}
