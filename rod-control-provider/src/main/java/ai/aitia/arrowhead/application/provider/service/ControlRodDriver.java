package ai.aitia.arrowhead.application.provider.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import eu.arrowhead.application.skeleton.provider.configuration.ConfigConstants;
import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.dto.shared.OrchestrationFlags.Flag;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO.Builder;
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;
import eu.arrowhead.common.dto.shared.ServiceInterfaceResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.exception.InvalidParameterException;

@Component
public class ControlRodDriver {

	// =================================================================================================
	// members

	@Autowired
	private ArrowheadService arrowheadService;

	@Autowired
	protected SSLProperties sslProperties;

	private final Logger logger = LogManager.getLogger(ControlRodDriver.class);

	// =================================================================================================
	// methods

	public int getLatestTemperatureReading() {
		final OrchestrationResultDTO orchestrationResult = orchestrate(
				ConfigConstants.GET_LATEST_TEMP_READING_DEFINITION);
		return consumeService(orchestrationResult);
	}

	public int getLatestPressureReading() {
		final OrchestrationResultDTO orchestrationResult = orchestrate(
				ConfigConstants.GET_LATEST_PRESSURE_READING_DEFINITION);
		return consumeService(orchestrationResult);
	}

	// =================================================================================================
	// assistant methods

	// -------------------------------------------------------------------------------------------------
	private OrchestrationResultDTO orchestrate(final String serviceDefinition) {
		final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(serviceDefinition)
				.interfaces(getInterface())
				.build();

		final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
		final OrchestrationFormRequestDTO orchestrationFormRequest = orchestrationFormBuilder
				.requestedService(serviceQueryForm)
				.flag(Flag.MATCHMAKING, true)
				.flag(Flag.OVERRIDE_STORE, true)
				.build();

		final OrchestrationResponseDTO orchestrationResponse = arrowheadService
				.proceedOrchestration(orchestrationFormRequest);

		if (orchestrationResponse == null) {
			logger.info("No orchestration response received");
		} else if (orchestrationResponse.getResponse().isEmpty()) {
			logger.info("No provider found during the orchestration");
		} else {
			final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
			validateOrchestrationResult(orchestrationResult, serviceDefinition);
			return orchestrationResult;
		}
		throw new ArrowheadException("Unsuccessful orchestration: " + serviceDefinition);
	}

	// -------------------------------------------------------------------------------------------------
	private int consumeService(final OrchestrationResultDTO orchestrationResult) {
		final String token = orchestrationResult.getAuthorizationTokens() == null ? null
				: orchestrationResult.getAuthorizationTokens().get(getInterface());

		return arrowheadService.consumeServiceHTTP(Integer.class,
				HttpMethod.valueOf(orchestrationResult.getMetadata().get(ConfigConstants.HTTP_METHOD)),
				orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(),
				orchestrationResult.getServiceUri(),
				getInterface(), token, null);
	}

	// -------------------------------------------------------------------------------------------------
	private String getInterface() {
		return sslProperties.isSslEnabled() ? ConfigConstants.INTERFACE_SECURE : ConfigConstants.INTERFACE_INSECURE;
	}

	// -------------------------------------------------------------------------------------------------
	private void validateOrchestrationResult(final OrchestrationResultDTO orchestrationResult,
			final String serviceDefinitin) {
		if (!orchestrationResult.getService().getServiceDefinition().equalsIgnoreCase(serviceDefinitin)) {
			throw new InvalidParameterException("Requested and orchestrated service definition do not match");
		}

		boolean hasValidInterface = false;
		for (final ServiceInterfaceResponseDTO serviceInterface : orchestrationResult.getInterfaces()) {
			if (serviceInterface.getInterfaceName().equalsIgnoreCase(getInterface())) {
				hasValidInterface = true;
				break;
			}
		}
		if (!hasValidInterface) {
			throw new InvalidParameterException("Requested and orchestrated interface do not match");
		}
	}
}
