package ai.aitia.arrowhead.application.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ai.aitia.arrowhead.application.provider.RodControllerConstants;
import ai.aitia.arrowhead.application.provider.service.RodControlService;
import ai.aitia.reactor_common.dto.RodInsertionResponseDTO;
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
	public RodInsertionResponseDTO getControlRodInsertion() {
		return rodControlService.calculateInsertion();
	}

	@GetMapping(path = RodControllerConstants.GET_PRESSURE_ACTION_URI)
	@ResponseBody
	public String getPressureAction() {
		return rodControlService.getPressureAction();
	}
}