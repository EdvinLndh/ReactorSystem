package ai.aitia.demo.energy_forecast.provider.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RodControlService {

	// =================================================================================================
	// members

	@Autowired
	private ControlRodDriver controlRodDriver;

	private final Logger logger = LogManager.getLogger(RodControlService.class);

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------
	// public EnergyForecastDTO forecast(final long building, final long
	// forecastedTimestamp)
	// throws IOException, URISyntaxException {
	// final List<String[]> dataSet = updateDataSet(building, forecastedTimestamp);
	// return predict(dataSet, building, forecastedTimestamp);
	// }

	public int calculateInsertion() {
		int latestTemp = controlRodDriver.getLatestTemperatureReading();

		if (latestTemp < 150) {
			logger.info("Reactor is still starting up, rod insertion: 10%");
			return 10;
		} else if (latestTemp < 200) {
			logger.info("Reactor is still starting up, rod insertion: 30%");
			return 30;
		} else if (latestTemp < 300) {
			logger.info("Reactor is still starting up, rod insertion: 40%");
			return 40;
		} else if (latestTemp >= 300) {
			logger.info("Reactor is getting warm, rod insertion: 60%");
			return 60;
		}
		return 0;
	}

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------

}
