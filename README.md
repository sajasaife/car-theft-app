# Car Theft API

This repository, named car-theft-app, hosts a simple API designed for retrieving information about car theft, with a specific focus on car brands and their countries of origin.

## API Endpoints
Get Country of Origin by Car Brand
GET /cars

### Parameters:
- __carBrand__ (*required*): The brand of the car for which you want to retrieve the country of origin.
-  __page__ (*optional*, *default*: *0*): The page number for paginated results.
- __pageSize__ (*optional*, *default*: *10*): The number of results per page.

## Example Request
GET /cars?carBrand=Toyota&page=0&pageSize=10

## Data Model
### Car Data
Represents information about a car, including its brand and country of origin.

#### Attributes:
- __id (*auto-generated*):__ The unique identifier for the car data.
- __carBrand:__ The brand of the car.
- __countryOfOrigin:__ The country of origin for the car.

## Code Structure
The project is structured into the following components:

### - Controller: CarDataController
Exposes RESTful endpoints for interacting with car data.

### - Model: CarData
Represents the structure of the car data and its attributes.

### - Repository: CarDataRepository
Defines JPA repository methods for accessing and querying car data.

### - Service: CarDataService
Provides business logic for retrieving car data based on car brand.

### - Util: CsvReaderUtil
A utility class for reading car data from a CSV file.

### - Application: CarTheftApiApplication
The main application class with the entry point and a command line runner for loading initial data from a CSV file.


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
