package service;

import domain.CustomerRecord;

public class CustomerDto {
    CustomerRecord customerRecord;

    public CustomerDto(String naam, int telefoonNummer, String email) {
        customerRecord = new CustomerRecord(naam, telefoonNummer, email);
    }
}
