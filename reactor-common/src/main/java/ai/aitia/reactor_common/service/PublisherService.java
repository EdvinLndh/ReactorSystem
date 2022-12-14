package ai.aitia.reactor_common.service;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Map;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import ai.aitia.arrowhead.application.library.util.ApplicationCommonConstants;
import ai.aitia.reactor_common.event.PresetEventType;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.shared.EventPublishRequestDTO;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;

@Service
public class PublisherService {

	@Value(ApplicationCommonConstants.$APPLICATION_SYSTEM_NAME)
	private String applicationSystemName;

	@Value(ApplicationCommonConstants.$APPLICATION_SERVER_ADDRESS_WD)
	private String applicationSystemAddress;

	@Value(ApplicationCommonConstants.$APPLICATION_SERVER_PORT_WD)
	private int applicationSystemPort;

	@Value(CommonConstants.$SERVER_SSL_ENABLED_WD)
	private boolean sslEnabled;

	@Autowired
	private ArrowheadService arrowheadService;

	private final Logger logger = LogManager.getLogger(PublisherService.class);

	// Sample implementation of event publishing of preset event types
	public void publish(final PresetEventType eventType, final Map<String, String> metadata, final String payload) {
		final EventPublishRequestDTO request = getPublishRequest(eventType, metadata, payload);
		logger.info("Publishing request of type: {}", eventType);
		arrowheadService.publishToEventHandler(request);
	}

	// =================================================================================================
	// assistant methods

	// -------------------------------------------------------------------------------------------------
	private EventPublishRequestDTO getPublishRequest(final PresetEventType eventType,
			final Map<String, String> metadata, final String payload) {
		logger.debug("getPublishRequest started...");

		final String timeStamp = Utilities.convertZonedDateTimeToUTCString(ZonedDateTime.now());

		final EventPublishRequestDTO publishRequestDTO = new EventPublishRequestDTO(
				eventType.getEventTypeName(),
				getSource(),
				metadata,
				payload,
				timeStamp);

		return publishRequestDTO;
	}

	// -------------------------------------------------------------------------------------------------
	private SystemRequestDTO getSource() {
		logger.debug("getSource started...");

		final SystemRequestDTO source = new SystemRequestDTO();
		source.setSystemName(applicationSystemName);
		source.setAddress(applicationSystemAddress);
		source.setPort(applicationSystemPort);
		if (sslEnabled) {
			source.setAuthenticationInfo(
					Base64.getEncoder().encodeToString(arrowheadService.getMyPublicKey().getEncoded()));
		}

		return source;
	}
}
