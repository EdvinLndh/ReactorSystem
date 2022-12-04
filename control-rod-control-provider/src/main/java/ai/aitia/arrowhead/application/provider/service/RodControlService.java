package ai.aitia.arrowhead.application.provider.service;

import java.util.concurrent.Flow.Publisher;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ai.aitia.arrowhead.application.provider.RodControllerConstants;
import ai.aitia.reactor_common.PublisherConstants;
import ai.aitia.reactor_common.dto.RodInsertionResponseDTO;

@Component
public class RodControlService {

	// =================================================================================================
	// members

	@Autowired
	private ControlRodDriver controlRodDriver;

	private final Logger logger = LogManager.getLogger(RodControlService.class);

	// =================================================================================================
	// methods
	public String getPressureAction() {
		int latestPressure = controlRodDriver.getLatestPressureReading();
		String response = null;

		if (latestPressure > RodControllerConstants.HI_PRESSURE_LEVELS) {
			logger.info("High pressure levels. Action: " + PublisherConstants.PRESSURE_ACTION_OPEN);
			response = PublisherConstants.PRESSURE_ACTION_OPEN;
		} else {
			logger.info("Normal pressure levels. Action: " + PublisherConstants.PRESSURE_ACTION_NONE);
			response = PublisherConstants.PRESSURE_ACTION_NONE;
		}

		return response;
	}

	public RodInsertionResponseDTO calculateInsertion() {
		int latestTemp = controlRodDriver.getLatestTemperatureReading();

		RodInsertionResponseDTO response = new RodInsertionResponseDTO();
		response.setPressureScale("MPa");
		response.setTemperatureScale("Celsius");

		if (latestTemp < RodControllerConstants.LOW_TEMP_LEVEL) {
			logger.info("Low reactor temperature, recommended rod insertion: 10%");
			response.setRodInsertionPrecentage(10);
		} else if (latestTemp < RodControllerConstants.MED_TEMP_LEVEL) {
			logger.info("Medium reactor temperature, recommended rod insertion: 30%");
			response.setRodInsertionPrecentage(30);
		} else if (latestTemp < RodControllerConstants.HI_TEMP_LEVEL) {
			logger.info("High reactor temperature, recommended rod insertion: 40%");
			response.setRodInsertionPrecentage(40);
		} else if (latestTemp >= RodControllerConstants.VERY_HI_TEMP_LEVEL) {
			logger.info("Very high reactor temperature, recommended rod insertion: 60%");
			response.setRodInsertionPrecentage(60);
		}

		response.setTemperatureReading(latestTemp);

		return response;
	}

}
