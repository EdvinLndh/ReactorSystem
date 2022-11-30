package ai.aitia.arrowhead.application.provider.service;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ai.aitia.arrowhead.application.provider.RodControllerConstants;
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

	public RodInsertionResponseDTO calculateInsertion() {
		int latestTemp = controlRodDriver.getLatestTemperatureReading();
		int latestPressure = controlRodDriver.getLatestPressureReading();

		RodInsertionResponseDTO response = new RodInsertionResponseDTO(latestTemp, latestPressure, 0);

		if (latestTemp < RodControllerConstants.LOW_TEMP_LEVEL) {
			logger.info("Reactor is still starting up, recommended rod insertion: 10%");
			response.setRodInsertionPrecentage(10);
		} else if (latestTemp < RodControllerConstants.MED_TEMP_LEVEL) {
			logger.info("Reactor is still starting up, recommended rod insertion: 30%");
			response.setRodInsertionPrecentage(30);
		} else if (latestTemp < RodControllerConstants.HI_TEMP_LEVEL) {
			logger.info("Reactor is still starting up, recommended rod insertion: 40%");
			response.setRodInsertionPrecentage(40);
		} else if (latestTemp >= RodControllerConstants.VERY_HI_TEMP_LEVEL) {
			logger.info("Reactor is getting warm, recommended rod insertion: 60%");
			response.setRodInsertionPrecentage(60);
		}

		return response;
	}

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------

}
