package domain;

public record HotelGuestRecord(String achternaam, String voornaam, Integer kamernummer, String telefoonnummer, String email) {

    @Override
    public String toString() {
        return achternaam + ", " + voornaam + ", " + kamernummer + ", " + telefoonnummer + ", " + email;
    }

}
