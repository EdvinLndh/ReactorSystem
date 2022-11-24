package eu.arrowhead.application.skeleton.subscriber;

import org.springframework.beans.factory.annotation.Value;

import ai.aitia.arrowhead.application.library.util.ApplicationCommonConstants;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.dto.shared.SubscriptionRequestDTO;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;

public class SubscriberUtilities {

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

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------
	public static SubscriptionRequestDTO createSubscriptionRequestDTO(final String eventType,
			final SystemRequestDTO subscriber, final String notificationUri) {
		final SubscriptionRequestDTO subscription = new SubscriptionRequestDTO(eventType.toUpperCase(),
				subscriber,
				null,
				SubscriberConstants.DEFAULT_EVENT_NOTIFICATION_BASE_URI + "/" + notificationUri,
				false,
				null,
				null,
				null);

		return subscription;
	}

}