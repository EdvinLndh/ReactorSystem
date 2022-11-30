package eu.arrowhead.application.skeleton.consumer;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import ai.aitia.arrowhead.application.library.util.ApplicationCommonConstants;
import ai.aitia.reactor_common.dto.RodInsertionResponseDTO;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.Utilities;
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

	private final Logger logger = LogManager.getLogger(ConsumerTaskRunner.class);

	@Override
	public void run() {
		OrchestrationResultDTO rodOrchestration = orchestrateRodService();
		while (true) {
			if (rodOrchestration != null) {
				try {

					RodInsertionResponseDTO rodInsertionResponse = consumeRodService(rodOrchestration);
					logger.info("Consumed rod insertion service. Inserting rods at {}%",
							rodInsertionResponse.getRodInsertionPrecentage());

				} catch (UnavailableServerException e) {
					logger.warn("Rod service unavaible, caught exception: {}", e.getMessage());
				}
			} else {
				logger.info("Trying to reach rod service again.");
				rodOrchestration = orchestrateRodService();
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.warn("Interrupted by {}", e);
			}
		}
	}

	public OrchestrationResultDTO orchestrateRodService() {
		final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();

		final ServiceQueryFormDTO requestedService = new ServiceQueryFormDTO();
		requestedService.setServiceDefinitionRequirement(ConsumerConstants.GET_ROD_INSERTION_SERVICE_DEFINITION);

		orchestrationFormBuilder.requestedService(requestedService)
				.flag(Flag.MATCHMAKING, true)
				.flag(Flag.OVERRIDE_STORE, true);

		final OrchestrationFormRequestDTO orchestrationRequest = orchestrationFormBuilder.build();
		printOut(orchestrationRequest);

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

	private void printOut(final Object object) {
		System.out.println(Utilities.toPrettyJson(Utilities.toJson(object)));
	}
}
