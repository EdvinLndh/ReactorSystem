package eu.arrowhead.application.skeleton.provider.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.*;
import org.springframework.stereotype.Component;

@Component
public class TemperatureDataService {

	private static final String csvPath = "temperatureData.csv";

	private final Logger logger = LogManager.getLogger(TemperatureDataService.class);

	public void writeTempReadingToCSV(int reading) {
		FileWriter csvWriter;

		try {
			csvWriter = new FileWriter(csvPath);

			logger.info("Writing new reading of {} to file.", reading);
			csvWriter.append(System.currentTimeMillis() + ", " + String.valueOf(reading));

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			logger.error("Something went wrong writing to csv file: {}.", e.getMessage());
		}
	}

	public int getLatestTempReadingFromCSV() {
		File csvFile = new File(csvPath);
		if (csvFile.isFile()) {
			// create BufferedReader and read data from csv

			try {
				BufferedReader csvReader = new BufferedReader(new FileReader(csvFile));
				String[] data = { "", "" };
				String row;
				while ((row = csvReader.readLine()) != null) {
					data = row.split(",");
				}
				csvReader.close();
				logger.info("Read {},{} from file.", data[0], data[1]);

				return Integer.valueOf(data[1].trim());
			} catch (IOException e) {
				logger.error("Something went wrong reading csv file: {}.", e.getMessage());
			}
		}
		return -1;
	}

}
