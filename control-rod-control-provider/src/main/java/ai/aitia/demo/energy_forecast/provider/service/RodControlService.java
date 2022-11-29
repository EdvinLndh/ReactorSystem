package ai.aitia.demo.energy_forecast.provider.service;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ai.aitia.demo.energy_forecast.provider.RodControllerConstants;

@Component
public class RodControlService {

	// =================================================================================================
	// members

	@Autowired
	private ControlRodDriver controlRodDriver;

	private final Logger logger = LogManager.getLogger(RodControlService.class);

	// =================================================================================================
	// methods

	public int calculateInsertion() {
		int latestTemp = controlRodDriver.getLatestTemperatureReading();

		if (latestTemp < RodControllerConstants.LOW_TEMP_LEVEL) {
			logger.info("Reactor is still starting up, rod insertion: 10%");
			return 10;
		} else if (latestTemp < RodControllerConstants.MED_TEMP_LEVEL) {
			logger.info("Reactor is still starting up, rod insertion: 30%");
			return 30;
		} else if (latestTemp < RodControllerConstants.HI_TEMP_LEVEL) {
			logger.info("Reactor is still starting up, rod insertion: 40%");
			return 40;
		} else if (latestTemp >= RodControllerConstants.VERY_HI_TEMP_LEVEL) {
			logger.info("Reactor is getting warm, rod insertion: 60%");
			return 60;
		}
		return 0;
	}

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------

}
