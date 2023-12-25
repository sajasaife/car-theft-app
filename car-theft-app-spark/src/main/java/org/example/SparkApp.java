package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;
import java.util.Map;

public class SparkApp {
    public static String mainDatasetPath = "src/main/resources/2015_State_Top10Report_wTotalThefts.csv";
    public static String updatedRecordsDatasetPath = "src/main/resources/Sheet1.csv";

    public static void main(String[] args) throws Exception {
        // Set up Spark configuration
        SparkConf conf = new SparkConf().setAppName("CarTheftsApp").setMaster("local[*]");

        // Create Spark session
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        // Task 1 - Process data - print cars per country
        SparkProcessor processor = new SparkProcessor(spark);
        Map<String, Dataset<Row>> result = processor.processData(mainDatasetPath);

        // Task 2 - Update data
        Dataset<Row>  updatedDF = processor.updateData(updatedRecordsDatasetPath, result.get("originalDF"));

        // Task 3 - SQL Part
        processor.calculations(updatedDF, result.get("carCountryDF"));

        // Stop the Spark session
        spark.stop();
    }
}
