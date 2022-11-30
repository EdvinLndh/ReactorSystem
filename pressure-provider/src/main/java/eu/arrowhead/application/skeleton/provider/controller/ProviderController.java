package eu.arrowhead.application.skeleton.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.arrowhead.application.skeleton.provider.configuration.ConfigConstants;
import eu.arrowhead.application.skeleton.provider.services.PressureDataService;
import eu.arrowhead.common.CommonConstants;

@RestController
public class ProviderController {

	// =================================================================================================
	// members

	@Autowired
	private PressureDataService dataService;

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------
	@GetMapping(path = CommonConstants.ECHO_URI)
	public String echoService() {
		return "Got it!";
	}

	// -------------------------------------------------------------------------------------------------
	@GetMapping(path = ConfigConstants.GET_LATEST_PRESSURE_URI)
	public int getLatestPressureReading() {
		return dataService.getLatestPressureReadingFromCSV();
	}
}
