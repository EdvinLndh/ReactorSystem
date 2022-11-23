package eu.arrowhead.application.skeleton.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.arrowhead.application.skeleton.provider.configuration.ConfigConstants;
import eu.arrowhead.application.skeleton.provider.services.TemperatureDataService;
import eu.arrowhead.common.CommonConstants;

@RestController
public class ProviderController {

	// =================================================================================================
	// members

	@Autowired
	private TemperatureDataService dataService;

	// TODO: add your variables here

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------
	@GetMapping(path = CommonConstants.ECHO_URI)
	public String echoService() {
		return "Got it!";
	}

	// -------------------------------------------------------------------------------------------------
	// TODO: implement here your provider related REST end points
	@GetMapping(path = ConfigConstants.GET_LATEST_TEMP_URI)
	public int getLatestTempReading() {
		return dataService.getLatestTempReadingFromCSV();
	}
}
