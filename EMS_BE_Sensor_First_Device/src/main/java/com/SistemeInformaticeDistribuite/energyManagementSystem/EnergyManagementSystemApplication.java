package com.SistemeInformaticeDistribuite.energyManagementSystem;

import com.SistemeInformaticeDistribuite.energyManagementSystem.service.CsvReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnergyManagementSystemApplication implements CommandLineRunner {
	@Autowired
	private CsvReader csvReader;
	public static void main(String[] args) {
		SpringApplication.run(EnergyManagementSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String csvFilePath = "./sensor.csv";
		csvReader.startCsvReading(csvFilePath);
	}
}
