package eu.arrowhead.application.skeleton.consumer;

import java.util.Base64;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import ai.aitia.arrowhead.application.library.ArrowheadService;
import ai.aitia.arrowhead.application.library.util.ApplicationCommonConstants;
import eu.arrowhead.application.skeleton.subscriber.ConfigEventProperties;
import eu.arrowhead.application.skeleton.subscriber.SubscriberConstants;
import eu.arrowhead.application.skeleton.subscriber.SubscriberUtilities;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;
import eu.arrowhead.common.exception.InvalidParameterException;

@SpringBootApplication
@EnableConfigurationProperties(ConfigEventProperties.class)
@ComponentScan(basePackages = { CommonConstants.BASE_PACKAGE, "ai.aitia" })
public class ConsumerMain implements ApplicationRunner {

	// =================================================================================================
	// members

	@Autowired
	private ArrowheadService arrowheadService;

	@Value(SubscriberConstants.$PRESET_EVENT_TYPES_WD)
	private String presetEvents;

	@Value(ApplicationCommonConstants.$APPLICATION_SYSTEM_NAME)
	private String applicationSystemName;

	@Value(ApplicationCommonConstants.$APPLICATION_SERVER_ADDRESS_WD)
	private String applicationSystemAddress;

	@Value(ApplicationCommonConstants.$APPLICATION_SERVER_PORT_WD)
	private int applicationSystemPort;

	@Value(CommonConstants.$SERVER_SSL_ENABLED_WD)
	private boolean sslEnabled;

	@Autowired
	private ConfigEventProperties configEventProperties;

	private final Logger logger = LogManager.getLogger(ConsumerMain.class);

	// =================================================================================================
	// methods

	// ------------------------------------------------------------------------------------------------
	public static void main(final String[] args) {
		SpringApplication.run(ConsumerMain.class, args);
	}

	// -------------------------------------------------------------------------------------------------
	@Override
	public void run(final ApplicationArguments args) throws Exception {
		subscribeToPresetEvents();
	}

	private void subscribeToPresetEvents() {

		final Map<String, String> eventTypeMap = configEventProperties.getEventTypeURIMap();

		if (eventTypeMap == null) {
			logger.info("No preset events to subscribe.");
		} else {

			final SystemRequestDTO subscriber = new SystemRequestDTO();
			subscriber.setSystemName(applicationSystemName);
			subscriber.setAddress(applicationSystemAddress);
			subscriber.setPort(applicationSystemPort);

			if (sslEnabled) {
				subscriber.setAuthenticationInfo(
						Base64.getEncoder().encodeToString(arrowheadService.getMyPublicKey().getEncoded()));
			}

			for (final String eventType : eventTypeMap.keySet()) {
				try {
					arrowheadService.unsubscribeFromEventHandler(eventType, applicationSystemName,
							applicationSystemAddress, applicationSystemPort);
				} catch (final Exception ex) {
					logger.info("Could not unsubscribe from EventType: " + eventType);
				}

				try {
					arrowheadService.subscribeToEventHandler(SubscriberUtilities.createSubscriptionRequestDTO(eventType,
							subscriber, eventTypeMap.get(eventType)));
					logger.info("Subscribing to {}", eventType);
				} catch (final InvalidParameterException ex) {
					if (ex.getMessage().contains("Subscription violates uniqueConstraint rules")) {
						logger.info("Subscription is already in DB");
					}
				} catch (final Exception ex) {
					logger.info("Could not subscribe to EventType: " + eventType);
				}
			}
		}
	}
}
