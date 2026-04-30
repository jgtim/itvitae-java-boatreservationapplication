package data;

import domain.HotelGuestRecord;

import java.util.ArrayList;

public interface CustomerDataHandler {

    public ArrayList<HotelGuestRecord> read();
}