package org.example;

import org.apache.spark.sql.*;

import java.util.HashMap;
import java.util.Map;

public class SparkProcessor {
    String outputsPath = "src/main/resources/outputs";
    private final SparkSession spark;
    public SparkProcessor(SparkSession spark) {
        this.spark = spark;
    }

    public Map<String, Dataset<Row>> processData(String csvFilePath) throws Exception {
        // Read CSV file in parallel by default
        Dataset<Row> df = spark.read().option("header", "true").csv(csvFilePath);

        df = cleanDataFrame(df);

        df.show(30);

        df.cache();

        // call api
        ApiClient apiClient = new ApiClient(spark);
        Dataset<Row> apiResultDF = apiClient.callApi(df);

        // Task1: get cars per country CSVs
        Dataset<Row> task1DF = apiResultDF.select("Make/Model", "CountryOfOrigin").distinct().repartition(10); // introduce parallelism by repartitioning after map
        task1DF.cache();

        saveResultAsCsv(task1DF, outputsPath + "/task1_cars_per_country");


        Map<String, Dataset<Row>> result = new HashMap<>();
        result.put("originalDF", df);
        result.put("apiDF", apiResultDF);
        result.put("carCountryDF", task1DF);

        df.unpersist();
        apiResultDF.unpersist();
        task1DF.unpersist();

        return result;
    }

    private void saveResultAsCsv(Dataset<Row> df, String outputFolder) {
        df.write()
                .mode("overwrite")
                .option("header", "false")
                .option("delimiter", ",")
                .partitionBy("CountryOfOrigin")
                .csv(outputFolder);
    }

    private Dataset<Row> cleanDataFrame(Dataset<Row> df) {
        df = df.withColumn("Thefts", functions.regexp_replace(functions.col("Thefts"), ",", ""));

        for (String column : df.columns()) {
            df = df.withColumn(column, functions.trim(df.col(column)));
        }
        return df;
    }

    public Dataset<Row> updateData(String updatedCsvFilePath, Dataset<Row> df) {
        // Read updated CSV file in parallel by default
        Dataset<Row> updatesDF = spark.read().option("header", "true").csv(updatedCsvFilePath);
        updatesDF.cache();

        updatesDF = cleanDataFrame(updatesDF);

        // Merge and display the updated DataFrame
        Dataset<Row> mergedDF = mergeDataFrames(df, updatesDF);

        updatesDF.unpersist();
        // Save the merged result as a CSV file in parallel
        mergedDF.write()
                .mode("overwrite")
                .option("header", "true")
                .option("delimiter", ",")
                .csv(outputsPath + "/task2_updated_dateset");

        return mergedDF;

    }

    public void calculations(Dataset<Row> df, Dataset<Row> df2, Dataset<Row> df3){
        // Register the DataFrame as a temporary SQL table
        df.createOrReplaceTempView("cars_thefts");
        df2.createOrReplaceTempView("cars_thefts_full");
        df3.createOrReplaceTempView("cars_countries");

        // Run the SQL query
        Dataset<Row> result1 = spark.sql(
                "SELECT `Make/Model` AS CarModel, SUM(Thefts) AS TotalThefts " +
                        "FROM cars_thefts " +
                        "GROUP BY `Make/Model` " +
                        "ORDER BY TotalThefts DESC " +
                        "LIMIT 5");

        result1.write()
                .mode("overwrite")
                .option("header", "true")
                .option("delimiter", ",")
                .csv(outputsPath + "/task3_top5_thefted_models");

        Dataset<Row> result2 = spark.sql(
                "SELECT State, SUM(Thefts) AS TotalThefts " +
                        "FROM cars_thefts " +
                        "GROUP BY State " +
                        "ORDER BY TotalThefts DESC " +
                        "LIMIT 5");

        result2.write()
                .mode("overwrite")
                .option("header", "true")
                .option("delimiter", ",")
                .csv(outputsPath + "/task4_top5_states_by_number_of_thefted_cars");


        Dataset<Row> result3 = spark.sql(
                "SELECT DISTINCT C.CountryOfOrigin, COUNT(T.`Make/Model`) AS TotalMakes " +
                        "FROM cars_countries C " +
                        "LEFT JOIN cars_thefts_full T ON C.`Make/Model` = T.`Make/Model` " +
                        "GROUP BY C.CountryOfOrigin " +
                        "ORDER BY TotalMakes DESC " +
                        "LIMIT 1");

        result3.write()
                .mode("overwrite")
                .option("header", "true")
                .option("delimiter", ",")
                .csv(outputsPath + "/task5_most_country_from_where_Americans_buy_their_cars");
    }
    private Dataset<Row> mergeDataFrames(Dataset<Row> df, Dataset<Row> updatesDF) {
        return df.as("original")
                .join(updatesDF.as("updated"),
                        functions.expr("original.State = updated.State AND " +
                                "original.`Make/Model` = updated.`Make/Model` AND " +
                                "original.`Model Year` = updated.`Model Year` AND " +
                                "original.Thefts = updated.Thefts"),
                        "left_outer")
                .selectExpr("COALESCE(updated.State, original.State) as State",
                        "COALESCE(updated.Rank, original.Rank) as Rank",
                        "COALESCE(updated.`Make/Model`, original.`Make/Model`) as `Make/Model`",
                        "COALESCE(updated.`Model Year`, original.`Model Year`) as `Model Year`",
                        "COALESCE(updated.Thefts, original.Thefts) as Thefts")
                .repartition(10); // introduce parallelism by repartitioning after select
    }

}

