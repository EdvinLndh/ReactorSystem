package eu.arrowhead.application.skeleton.provider;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import eu.arrowhead.application.skeleton.provider.configuration.ConfigConstants;
import eu.arrowhead.application.skeleton.provider.services.TemperatureDataService;
import eu.arrowhead.application.skeleton.publisher.event.PresetEventType;
import eu.arrowhead.application.skeleton.publisher.service.PublisherService;

public class TemperatureGenerator extends Thread {

	private Random rand;

	private boolean shutdownActive;

	private double linearFactor;

	@Autowired
	private TemperatureDataService dataService;

	@Autowired
	private PublisherService publisherService;

	private final Logger logger = LogManager.getLogger(TemperatureGenerator.class);

	public TemperatureGenerator() {
		this.rand = new Random();
		this.shutdownActive = false;
		this.linearFactor = 1;
	}

	@Override
	public void run() {
		while (true) {

			int latestReading = dataService.getLatestTempReadingFromCSV();
			int randNum = rand.nextInt(latestReading);
			int newReading = (int) Math.round(latestReading * this.linearFactor) + randNum;
			logger.info("Current reactor temperature: {}", latestReading);

			dataService.writeTempReadingToCSV(newReading);

			// DEBUG
			publisherService.sendNotification();

			if (newReading > ConfigConstants.CRITICAL_TEMPERAUTRE) {
				// Publish
				publisherService.publish(PresetEventType.CRITICAL_TEMPERATURE, null,
						"Critical temperatures have been reached.");
				logger.warn("Critical temperature {}, must publish!", newReading);

				setShutdownActive(true);
				setLinearFactor(0.1);
			} else if (newReading > (int) Math.round(ConfigConstants.CRITICAL_TEMPERAUTRE / 2)) {
				setLinearFactor(0.3);
			} else if (newReading > (int) Math.round(ConfigConstants.CRITICAL_TEMPERAUTRE / 3)) {
				setLinearFactor(0.5);
			} else if (newReading <= (int) Math.round(ConfigConstants.CRITICAL_TEMPERAUTRE / 3)) {
				setLinearFactor(1);
			}
			// latestReading = newReading;
			try {
				Thread.sleep(ConfigConstants.SLEEP_TIME_MILLIS);
			} catch (InterruptedException e) {
				logger.error("Thread interrupted");
			}

		}

	}

	public boolean isShutdownActive() {
		return shutdownActive;
	}

	public void setShutdownActive(boolean shutdownActive) {
		this.shutdownActive = shutdownActive;
	}

	public double getLinearFactor() {
		return linearFactor;
	}

	public void setLinearFactor(double linearFactor) {
		this.linearFactor = linearFactor;
	}

}
