package eu.arrowhead.application.skeleton.subscriber.controller;

import org.apache.logging.log4j.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.arrowhead.application.skeleton.subscriber.*;
import eu.arrowhead.common.dto.shared.EventDTO;

@RestController
@RequestMapping(SubscriberConstants.DEFAULT_EVENT_NOTIFICATION_BASE_URI)
public class ConsumerController {

	private final Logger logger = LogManager.getLogger(ConsumerController.class);

	// -------------------------------------------------------------------------------------------------
	@PostMapping(path = SubscriberConstants.REQUEST_RECEIVED_NOTIFICATION_URI)
	public void receiveEventRequestReceived(@RequestBody final EventDTO event) {
		logger.info("receiveEventRequestReceived started...");

	}

	// -------------------------------------------------------------------------------------------------
	@PostMapping(path = SubscriberConstants.PUBLISHER_DESTORYED_NOTIFICATION_URI)
	public void receiveEventDestroyed(@RequestBody final EventDTO event) {
		logger.info("receiveEventDestroyed started... ");

	}

	// -------------------------------------------------------------------------------------------------
	@PostMapping(path = SubscriberConstants.CRITICAL_TEMPERATURE_NOTIFICATION_URI)
	public void criticalTempEventReceived(@RequestBody final EventDTO event) {
		logger.debug("criticalTempEventReceived started... ");
		logger.info("Received critical temperature event. Inserting all rods.");
	}
}
