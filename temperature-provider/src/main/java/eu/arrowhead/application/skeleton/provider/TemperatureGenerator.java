package eu.arrowhead.application.skeleton.provider;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import eu.arrowhead.application.skeleton.provider.configuration.ConfigConstants;
import eu.arrowhead.application.skeleton.provider.services.TemperatureDataService;

public class TemperatureGenerator extends Thread {

	private Random rand;

	private boolean shutdownActive;

	private double linearFactor;

	@Autowired
	private TemperatureDataService dataService;

	private final Logger logger = LogManager.getLogger(TemperatureGenerator.class);

	public TemperatureGenerator() {
		this.rand = new Random();
		this.shutdownActive = false;
		this.linearFactor = 1;
	}

	@Override
	public void run() {
		int latestReading = dataService.getLatestTempReadingFromCSV();

		while (true) {
			int randNum = rand.nextInt(latestReading);
			int newReading = (int) Math.round(latestReading * this.linearFactor) + randNum;

			logger.info("Read temperature: {}", newReading);
			dataService.writeTempReadingToCSV(newReading);

			if (newReading > ConfigConstants.CRITICAL_TEMPERAUTRE) {
				// Publish
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
			latestReading = newReading;
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
