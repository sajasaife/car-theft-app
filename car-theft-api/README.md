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

