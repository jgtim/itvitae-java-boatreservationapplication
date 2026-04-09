package data;

import domain.BoatType;
import domain.LakeArea;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import service.Boat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BoatDataExcel implements BoatDataHandler {
    final String FILEPATH = "data/Bootverhuur_Prices.xlsx";

    @Override
    public ArrayList<Boat> read() {
        var boatList = new ArrayList<Boat>();

        try (InputStream is = new FileInputStream(FILEPATH); ReadableWorkbook wb = new ReadableWorkbook(is)) {
            Sheet sheet = wb.getFirstSheet();

            List<Row> rows = sheet.read();

            int r = 1;
            int boatId = 1;
            for (Row row : rows) {
                if (r > 1) {
                    int numberOfBoats = row.getCellAsNumber(2).orElse(BigDecimal.valueOf(0)).intValue();

                    if (numberOfBoats > 0) {

                        String typeId = row.getCellAsString(0).orElse("");
                        int capacity = row.getCellAsNumber(3).orElse(BigDecimal.valueOf(0)).intValue();
                        String area = row.getCellAsString(4).orElse("");
                        boolean isElectric = row.getCellAsString(5).orElse("").equalsIgnoreCase("yes");
                        boolean hasLifeJackets = row.getCellAsString(6).orElse("").equalsIgnoreCase("yes");
                        double price15 = row.getCellAsNumber(8).orElse(BigDecimal.valueOf(0)).doubleValue();
                        double price30 = row.getCellAsNumber(9).orElse(BigDecimal.valueOf(0)).doubleValue();

                        for (int i = 1; i <= numberOfBoats; i++) {
                            BoatType boatType = switch (typeId) {
                                case "B-01" -> BoatType.STANDARD_ROWBOAT;
                                case "B-02" -> BoatType.FAMILY_ROWBOAT;
                                case "B-03" -> BoatType.KAYAK;
                                case "B-04" -> BoatType.PEDAL;
                                case "B-05" -> BoatType.SILENT_ELECTRIC;
                                case "B-06" -> BoatType.PREMIUM_ELECTRIC;
                                default -> null;
                            };

                            area = area.toUpperCase().replace(" ", "_");
                            LakeArea lakeArea;
                            try {
                                lakeArea = LakeArea.valueOf(area);
                            } catch (IllegalArgumentException e) {
                                lakeArea = null;
                            }

                            String boatName = boatType != null? boatType.getDisplayBoatType() + " " + i : "Onbekende Boot" + i;
                            boatList.add(new Boat(boatType, boatName, lakeArea, capacity, isElectric, hasLifeJackets, price15, price30, boatId));
                            boatId++;
                        }
                    }
                }
                r++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return boatList;
    }
}
