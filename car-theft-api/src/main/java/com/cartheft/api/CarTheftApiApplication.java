package com.cartheft.api;

import com.cartheft.api.model.CarData;
import com.cartheft.api.repository.CarDataRepository;
import com.cartheft.api.util.CsvReaderUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CarTheftApiApplication {

	private final CarDataRepository carRepository;

	public CarTheftApiApplication(CarDataRepository carRepository) {
		this.carRepository = carRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(CarTheftApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData() {
		return args -> {
			// Load data from CSV file using CsvReaderUtil
			String csvFilePath = "src/main/resources/cars.csv";
			List<CarData> carDataList = CsvReaderUtil.readCarDataFromCsv(csvFilePath);

			// Process carDataList and save to the database
			carRepository.saveAll(carDataList);
		};
	}

}
