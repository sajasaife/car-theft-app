package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.split;

public class ApiClient implements Serializable {
    private final SparkSession spark;

    public ApiClient(SparkSession spark) {
        this.spark = spark;
    }

    public Dataset<Row> callApi(Dataset<Row> dataFrame) throws Exception {
        // Define a UDF to call the API and get the response
        spark.udf().register("callApi", new UDF1<String, String>() {
            @Override
            public String call(String model) throws Exception {
                // Check if the model is not null
                if (model == null) {
                    return null;
                }

                String apiUrl = "http://localhost:8080/cars?carBrand=" + model + "&page=0&size=0";

                // Call the API and get the response
                String apiResponse = makeApiCall(apiUrl);

                // Parse the JSON response and extract the desired value
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(apiResponse);

                // Make sure 'content' and 'countryOfOrigin' exist in the response
                JsonNode contentNode = jsonNode.get("content");
                if (contentNode != null && contentNode.isArray() && contentNode.size() > 0) {
                    JsonNode countryOfOriginNode = contentNode.get(0).get("countryOfOrigin");
                    if (countryOfOriginNode != null) {
                        return countryOfOriginNode.asText();
                    }
                }

                // Return null if the structure is not as expected
                return null;
            }
        }, DataTypes.StringType);
        return  dataFrame.withColumn("CountryOfOrigin", functions.callUDF("callApi", split(col("Make/Model"), " ").getItem(0))).repartition();
    }

    private static String makeApiCall(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            throw new RuntimeException("API call failed with response code: " + responseCode);
        }
    }
}

