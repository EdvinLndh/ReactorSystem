package ai.aitia.demo.energy_forecast.provider.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ai.aitia.demo.energy_forecast.provider.RodControllerConstants;
import ai.aitia.demo.energy_forecast.provider.service.RodControlService;
import eu.arrowhead.common.exception.BadPayloadException;

@RestController
public class RodControllerController {

	// =================================================================================================
	// members

	@Autowired
	private RodControlService rodControlService;

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------

	@GetMapping(path = RodControllerConstants.GET_ROD_INSERTION_URI)
	@ResponseBody
	public int getControlRodInsertion() {
		return rodControlService.calculateInsertion();
	}
}