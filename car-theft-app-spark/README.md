# Spark Car Thefts Application

This Spark application, designed for processing and analyzing car theft data, comprises several components to achieve specific tasks. The application is built using Apache Spark and Java.

## ApiClient
The __ApiClient__ class is responsible for making API calls based on car model information. It utilizes Apache Spark's User-Defined Function (UDF) to call a specified API and parse the JSON response. The resulting country of origin information is then added to the input DataFrame.

## SparkProcessor
The __SparkProcessor__ class encapsulates various tasks related to data processing, updating, and SQL-based calculations. It operates on Spark DataFrames and performs operations such as cleaning, merging, and saving results to CSV files.

## SparkApp
The __SparkApp__ class serves as the entry point for the Spark application. It sets up the Spark configuration, creates a Spark session, and orchestrates tasks related to processing and updating car theft data.

## Key Tasks:
- Task 1 - Process Data:

  - Reads a CSV file in parallel and cleans the DataFrame.
  - Calls the ApiClient to add country of origin information to the DataFrame.
  - Saves the results as CSV files.
- Task 2 - Update Data:

  - Reads an updated CSV file, cleans it, and merges it with the original DataFrame.
  - Saves the merged result as a CSV file.
- Task 3 - SQL Calculations:

  - Performs SQL-based calculations on the DataFrame.
  - Saves the results as CSV files.
