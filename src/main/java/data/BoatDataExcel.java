package data;

import domain.BoatRecord;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import service.Boat;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BoatDataExcel implements BoatDataHandler {
    final String FILEPATH = "data/Bootverhuur_Prices.xlsx";

    @Override
    public ArrayList<Boat> read() {
        var boatList = new ArrayList<Boat>();

        try (InputStream is = new FileInputStream(FILEPATH); ReadableWorkbook wb = new ReadableWorkbook(is)) {
            Sheet sheet = wb.getFirstSheet();

            List<Row> rows = sheet.read();

            int r = 1;
            for (Row row : rows) {
                if (r > 1) {
                    String typeId = row.getCellAsString(0).orElse("B-00");
                    String boatType = row.getCellAsString(1).orElse("Onbekend");
                    int numberOfBoats = row.getCellAsNumber(2).orElse(BigDecimal.valueOf(0)).intValue();
                    int capacity = row.getCellAsNumber(3).orElse(BigDecimal.valueOf(0)).intValue();
                    String lakeArea = row.getCellAsString(4).orElse("Onbekend");
                    boolean electric = row.getCellAsString(5).orElse("").equalsIgnoreCase("yes");
                    boolean lifeJackets = row.getCellAsString(6).orElse("").equalsIgnoreCase("yes");
                    String comments = row.getCellAsString(7).orElse(null);
                    double price15 = row.getCellAsNumber(8).orElse(BigDecimal.valueOf(0)).doubleValue();
                    double price30 = row.getCellAsNumber(9).orElse(BigDecimal.valueOf(0)).doubleValue();

                    if (numberOfBoats > 0) {
                        for (int i = 1; i <= numberOfBoats; i++) {
                            String boatId = typeId + "-" + i;
                            String boatName = boatType + " " + i;
                            boatList.add(new Boat(boatName, capacity, price15, price30, boatType, comments));
                        }
                    }
                }
                r++;
            }
        }




        return null;
    }

    public static void main(String[] args) {

    }
}
