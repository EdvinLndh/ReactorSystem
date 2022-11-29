package eu.arrowhead.application.skeleton.provider;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import eu.arrowhead.application.skeleton.provider.configuration.ConfigConstants;
import eu.arrowhead.application.skeleton.provider.services.TemperatureDataService;
import eu.arrowhead.application.skeleton.publisher.PublisherConstants;
import eu.arrowhead.application.skeleton.publisher.event.PresetEventType;
import eu.arrowhead.application.skeleton.publisher.service.PublisherService;

public class TemperatureGenerator extends Thread {

	private Random rand;

	private double linearFactor;

	@Autowired
	private TemperatureDataService dataService;

	@Autowired
	private PublisherService publisherService;

	private final Logger logger = LogManager.getLogger(TemperatureGenerator.class);

	public TemperatureGenerator() {
		this.rand = new Random();
		this.linearFactor = 1;
	}

	@Override
	public void run() {
		while (true) {

			int latestReading = dataService.getLatestTempReadingFromCSV();
			int randNum = rand.nextInt(latestReading);
			int newReading = (int) Math.round(latestReading * this.linearFactor) + randNum;
			logger.info("Current reactor temperature: {}", newReading);

			dataService.writeTempReadingToCSV(newReading);

			if (newReading > ConfigConstants.CRITICAL_TEMPERAUTRE) {
				// Publish
				publisherService.publish(PresetEventType.CRITICAL_TEMPERATURE, null,
						PublisherConstants.CRITICAL_TEMPERATURE_EVENT_PAYLOAD);
				logger.warn("Critical temperature {}, must publish!", newReading);

				setLinearFactor(0.1);
			} else if (newReading > (int) Math.round(ConfigConstants.CRITICAL_TEMPERAUTRE / 2)) {
				setLinearFactor(0.3);
			} else if (newReading > (int) Math.round(ConfigConstants.CRITICAL_TEMPERAUTRE / 3)) {
				setLinearFactor(0.5);
			} else if (newReading <= (int) Math.round(ConfigConstants.CRITICAL_TEMPERAUTRE / 3)) {
				setLinearFactor(1);
			}
			try {
				Thread.sleep(ConfigConstants.SLEEP_TIME_MILLIS);
			} catch (InterruptedException e) {
				logger.error("Thread interrupted: {}", e.getMessage());
			}
		}
	}

	public double getLinearFactor() {
		return linearFactor;
	}

	public void setLinearFactor(double linearFactor) {
		this.linearFactor = linearFactor;
	}

}
