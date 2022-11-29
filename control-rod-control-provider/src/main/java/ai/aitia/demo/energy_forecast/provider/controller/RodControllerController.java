package ai.aitia.demo.energy_forecast.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ai.aitia.demo.energy_forecast.provider.RodControllerConstants;
import ai.aitia.demo.energy_forecast.provider.service.RodControlService;
import eu.arrowhead.common.CommonConstants;

@RestController
public class RodControllerController {

	// =================================================================================================
	// members

	@Autowired
	private RodControlService rodControlService;

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------

	@GetMapping(path = CommonConstants.ECHO_URI)
	public String echoService() {
		return "Got it!";
	}

	@GetMapping(path = RodControllerConstants.GET_ROD_INSERTION_URI)
	@ResponseBody
	public int getControlRodInsertion() {
		return rodControlService.calculateInsertion();
	}
}