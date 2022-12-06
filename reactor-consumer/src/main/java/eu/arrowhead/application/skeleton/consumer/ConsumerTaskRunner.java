package eu.arrowhead.application.skeleton.consumer;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import ai.aitia.arrowhead.application.library.util.ApplicationCommonConstants;
import ai.aitia.reactor_common.PublisherConstants;
import ai.aitia.reactor_common.dto.PressureActionResponseDTO;
import ai.aitia.reactor_common.dto.RodInsertionResponseDTO;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO;
import eu.arrowhead.common.dto.shared.OrchestrationFlags.Flag;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO.Builder;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.exception.UnavailableServerException;

public class ConsumerTaskRunner extends Thread {

	@Autowired
	private ArrowheadService arrowheadService;

	@Autowired
	protected SSLProperties sslProperties;

	@Value(ApplicationCommonConstants.$TOKEN_SECURITY_FILTER_ENABLED_WD)
	private boolean tokenSecurityFilterEnabled;

	@Value(CommonConstants.$SERVER_SSL_ENABLED_WD)
	private boolean sslEnabled;

	@Value(ApplicationCommonConstants.$APPLICATION_SYSTEM_NAME)
	private String applicationSystemName;

	@Value(ApplicationCommonConstants.$APPLICATION_SERVER_ADDRESS_WD)
	private String applicationSystemAddress;

	@Value(ApplicationCommonConstants.$APPLICATION_SERVER_PORT_WD)
	private int applicationSystemPort;

	private boolean temperatureProviderActive = true;

	private boolean pressureProviderActive = true;

	private final Logger logger = LogManager.getLogger(ConsumerTaskRunner.class);

	@Override
	public void run() {
		OrchestrationResultDTO rodOrchestration = orchestrateService(
				PublisherConstants.GET_ROD_INSERTION_SERVICE_DEFINITION);
		OrchestrationResultDTO pressureOrchestration = orchestrateService(
				PublisherConstants.GET_PRESSURE_ACTION_SERVICE_DEFINITION);
		while (true) {
			if (rodOrchestration != null && pressureOrchestration != null && this.temperatureProviderActive
					&& this.pressureProviderActive) {
				try {

					RodInsertionResponseDTO rodInsertionResponse = consumeRodService(rodOrchestration);
					PressureActionResponseDTO pressureResponse = consumePressureService(pressureOrchestration);
					logger.info(
							"Consumed rod insertion service. \n" + tabs() + "Temperature at: {} {}\n" + tabs()
									+ "Pressure at: {} {}\n" + tabs() + "Inserting rods at {}%",
							rodInsertionResponse.getTemperatureReading(), rodInsertionResponse.getTemperatureScale(),
							pressureResponse.getPressureReading(), pressureResponse.getPressureScale(),
							rodInsertionResponse.getRodInsertionPrecentage());

					logger.info("Taking pressure action: " + pressureResponse.getPressureAction());

				} catch (UnavailableServerException e) {
					logger.warn("Rod service unavaible, caught exception: {}", e.getMessage());
					temperatureProviderActive = false;
					pressureProviderActive = false;
				}
			} else {
				logger.info("Some required systems might be down, trying to reorchestrate to rod service.");
				rodOrchestration = orchestrateService(PublisherConstants.GET_ROD_INSERTION_SERVICE_DEFINITION);
				pressureOrchestration = orchestrateService(
						PublisherConstants.GET_PRESSURE_ACTION_SERVICE_DEFINITION);
			}

			try {
				Thread.sleep(ConsumerConstants.SLEEP_TIME_MILLIS);
			} catch (InterruptedException e) {
				logger.warn("Interrupted by {}", e);
			}
		}
	}

	private String tabs() {
		return "\t\t\t\t\t\t\t\t\t\t\t\t\t";
	}

	public OrchestrationResultDTO orchestrateService(String service_definition) {
		final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();

		final ServiceQueryFormDTO requestedService = new ServiceQueryFormDTO();
		requestedService.setServiceDefinitionRequirement(service_definition);

		orchestrationFormBuilder.requestedService(requestedService)
				.flag(Flag.MATCHMAKING, true)
				.flag(Flag.OVERRIDE_STORE, true);

		final OrchestrationFormRequestDTO orchestrationRequest = orchestrationFormBuilder.build();

		OrchestrationResponseDTO response = null;
		try {
			response = arrowheadService.proceedOrchestration(orchestrationRequest);
		} catch (final ArrowheadException e) {
			logger.error("Exception caught: {}", e.getMessage());
		}

		if (response == null || response.getResponse().isEmpty()) {

			logger.info("Orchestration response is empty");
			return null;
		}

		final OrchestrationResultDTO result = response.getResponse().get(0);
		return result;
	}

	public RodInsertionResponseDTO consumeRodService(OrchestrationResultDTO result) throws UnavailableServerException {
		final HttpMethod httpMethod = HttpMethod.GET;
		final String address = result.getProvider().getAddress();
		final int port = result.getProvider().getPort();
		final String serviceUri = result.getServiceUri();
		final String interfaceName = result.getInterfaces().get(0).getInterfaceName();
		String token = null;
		if (result.getAuthorizationTokens() != null) {
			token = result.getAuthorizationTokens().get(interfaceName);
		}
		final Object payload = null;

		final RodInsertionResponseDTO consumedService = arrowheadService.consumeServiceHTTP(
				RodInsertionResponseDTO.class, httpMethod,
				address, port,
				serviceUri, interfaceName, token, payload);

		return consumedService;

	}

	public PressureActionResponseDTO consumePressureService(OrchestrationResultDTO result)
			throws UnavailableServerException {
		final HttpMethod httpMethod = HttpMethod.GET;
		final String address = result.getProvider().getAddress();
		final int port = result.getProvider().getPort();
		final String serviceUri = result.getServiceUri();
		final String interfaceName = result.getInterfaces().get(0).getInterfaceName();
		String token = null;
		if (result.getAuthorizationTokens() != null) {
			token = result.getAuthorizationTokens().get(interfaceName);
		}
		final Object payload = null;

		final PressureActionResponseDTO consumedService = arrowheadService.consumeServiceHTTP(
				PressureActionResponseDTO.class, httpMethod,
				address, port,
				serviceUri, interfaceName, token, payload);

		return consumedService;

	}

	public boolean isPressureProviderActive() {
		return pressureProviderActive;
	}

	public void setPressureProviderActive(boolean pressureProviderActive) {
		this.pressureProviderActive = pressureProviderActive;
	}

	public boolean isTemperatureProviderActive() {
		return temperatureProviderActive;
	}

	public void setTemperatureProviderActive(boolean temperatureProviderActive) {
		this.temperatureProviderActive = temperatureProviderActive;
	}
}
