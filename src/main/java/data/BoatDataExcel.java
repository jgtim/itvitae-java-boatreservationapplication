package data;

import domain.BoatRecord;
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
 * This class handles communication of the boat data between the application and the Excel file containing the boat data
 */
public class BoatDataExcel implements BoatDataHandler {
    //Path to the Excel file
    private final String FILEPATH = "data/Bootverhuur_Prices.xlsx";

    /**
     * Creates a list of boat objects from the Excel boat data.
     * @return Arraylist containing boat objects.
     */
    @Override
    public ArrayList<BoatRecord> read() {
        var boatList = new ArrayList<BoatRecord>();

        //Open the Excel file using fastexcel reader
        try (InputStream is = new FileInputStream(FILEPATH); ReadableWorkbook wb = new ReadableWorkbook(is)) {

            //Read the sheet with the boat data into rows
            Sheet sheet = wb.getFirstSheet();
            List<Row> rows = sheet.read();

            int r = 1;

            //Loop through all rows in the sheet, each row contains 1 type of boat
            for (Row row : rows) {
                if (r > 1) { //Ignore the first row, it contains headers

                    //Get the number of boats in the current row
                    int numberOfBoats = row.getCellAsNumber(2).orElse(BigDecimal.valueOf(0)).intValue();

                    //If the number of boats is 0 or can't be read, ignore the row
                    if (numberOfBoats > 0) {

                        //Load the rest of the data from the current row
                        String typeId = row.getCellAsString(0).orElse("");
                        int capacity = row.getCellAsNumber(3).orElse(BigDecimal.valueOf(0)).intValue();
                        String area = row.getCellAsString(4).orElse("");
                        boolean isElectric = row.getCellAsString(5).orElse("").equalsIgnoreCase("yes");
                        boolean hasLifeJackets = row.getCellAsString(6).orElse("").equalsIgnoreCase("yes");
                        double price15 = row.getCellAsNumber(8).orElse(BigDecimal.valueOf(0)).doubleValue();
                        double price30 = row.getCellAsNumber(9).orElse(BigDecimal.valueOf(0)).doubleValue();

                        //Create a boat object for each boat in this row
                        for (int i = 1; i <= numberOfBoats; i++) {

                            //Convert the type ID to the correct type enum value
                            BoatType boatType = switch (typeId) {
                                case "B-01" -> BoatType.STANDARD_ROWBOAT;
                                case "B-02" -> BoatType.FAMILY_ROWBOAT;
                                case "B-03" -> BoatType.KAYAK;
                                case "B-04" -> BoatType.PEDAL;
                                case "B-05" -> BoatType.SILENT_ELECTRIC;
                                case "B-06" -> BoatType.PREMIUM_ELECTRIC;
                                default -> null;
                            };

                            //Convert the lake area string to the correct enum value
                            area = area.toUpperCase().replace(" ", "_");
                            LakeArea lakeArea;
                            try {
                                lakeArea = LakeArea.valueOf(area);
                            } catch (IllegalArgumentException e) {
                                lakeArea = null;
                            }

                            //Get the human-readable boat name
                            String boatName = boatType != null? boatType.getDisplayBoatType() + " " + i : "Onbekende Boot" + i;

                            //Add the boat to the list
                            boatList.add(new BoatRecord(boatType, boatName, lakeArea, capacity, isElectric, hasLifeJackets, price15, price30));
                        }
                    }
                }
                r++;
            }

        //Catch exceptions with opening or reading the file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return boatList; //Return the list with boat objectss
    }
}
