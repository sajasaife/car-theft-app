package com.cartheft.api.util;

import com.cartheft.api.model.CarData;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderUtil {

    private CsvReaderUtil() {
        // private constructor to prevent instantiation
    }

    public static List<CarData> readCarDataFromCsv(String csvFilePath) {
        List<CarData> carDataList = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {
                if (record.length == 2) { // Assuming two columns: Car Brand, Country of Origin
                    CarData carData = new CarData(record[0], record[1]);
                    carDataList.add(carData);
                } else {
                    // Handle invalid records as needed
                    System.out.println("Invalid record: " + String.join(",", record));
                }
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace(); // Handle exceptions according to your application's needs
        }

        return carDataList;
    }
}
