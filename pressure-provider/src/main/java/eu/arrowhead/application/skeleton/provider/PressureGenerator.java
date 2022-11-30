package eu.arrowhead.application.skeleton.provider;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import ai.aitia.reactor_common.PublisherConstants;
import ai.aitia.reactor_common.event.PresetEventType;
import ai.aitia.reactor_common.service.PublisherService;
import eu.arrowhead.application.skeleton.provider.configuration.ConfigConstants;
import eu.arrowhead.application.skeleton.provider.services.PressureDataService;

public class PressureGenerator extends Thread {

	private Random rand;

	private double linearFactor;

	@Autowired
	private PressureDataService dataService;

	@Autowired
	private PublisherService publisherService;

	private final Logger logger = LogManager.getLogger(PressureGenerator.class);

	public PressureGenerator() {
		this.rand = new Random();
		this.linearFactor = 1;
	}

	@Override
	public void run() {
		while (true) {

			int latestReading = dataService.getLatestPressureReadingFromCSV();
			int randNum = rand.nextInt(latestReading);
			int newReading = (int) Math.round(latestReading * this.linearFactor) + randNum;
			logger.info("Current reactor pressure: {}", newReading);

			dataService.writePressureReadingToCSV(newReading);

			if (newReading > ConfigConstants.CRITICAL_PRESSURE) {
				// Publish
				publisherService.publish(PresetEventType.CRITICAL_PRESSURE, null,
						"Critical pressure levels: " + newReading);
				logger.warn("Critical pressure {}, must publish!", newReading);

				setLinearFactor(0.1);
			} else if (newReading > (int) Math.round(ConfigConstants.CRITICAL_PRESSURE / 2)) {
				setLinearFactor(0.3);
			} else if (newReading > (int) Math.round(ConfigConstants.CRITICAL_PRESSURE / 3)) {
				setLinearFactor(0.5);
			} else if (newReading <= (int) Math.round(ConfigConstants.CRITICAL_PRESSURE / 3)) {
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
