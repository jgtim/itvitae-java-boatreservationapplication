package service;

import domain.CustomerRecord;

public class Customer {
    CustomerRecord customerRecord;

    public Customer(String naam, int telefoonNummer, String email) {
        customerRecord = new CustomerRecord(naam, telefoonNummer, email);
    }
}
