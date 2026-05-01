package data;

import domain.HotelGuestRecord;
import domain.BoatType;
import domain.LakeArea;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles communication of the customer data between the application and the Excel file containing the customer data
 */
public class CustomerDataExcel implements CustomerDataHandler {
    //Path to the Excel file
    private final String FILEPATH = "data/hotel_data.xlsx";

    /**
     * Creates a list of hotel guest objects from the Excel guest data.
     * @return Arraylist containing hotel guest objects.
     */
    @Override
    public ArrayList<HotelGuestRecord> read() {
        var guestList = new ArrayList<HotelGuestRecord>();

        //Open the Excel file using fastexcel reader
        try (InputStream is = new FileInputStream(FILEPATH); ReadableWorkbook wb = new ReadableWorkbook(is)) {

            //Read the sheet with the customer data into rows
            Sheet sheet = wb.getFirstSheet();
            List<Row> rows = sheet.read();

            int r = 1;

            //Loop through all rows in the sheet, each row contains 1 guest
            for (Row row : rows) {
                if (r > 1) { //Ignore the first row, it contains headers


                    //Load the data from the current row
                    String lastName = row.getCellAsString(0).orElse("");
                    String firstName = row.getCellAsString(1).orElse("");
                    int roomNumber = row.getCellAsNumber(2).orElse(BigDecimal.valueOf(0)).intValue();
                    //NOTE may want this to be an actual number instead
                    String phoneNumber = row.getCellAsString(3).orElse("");
                    String email = row.getCellAsString(4).orElse("");

                    //Add the guest to the list
                    guestList.add(new HotelGuestRecord(
                            lastName,
                            firstName,
                            roomNumber,
                            phoneNumber,
                            email
                    ));
                }
                r++;
            }

            //Catch exceptions with opening or reading the file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return guestList; //Return the list with guest objects
    }
}
