package eu.arrowhead.application.skeleton.subscriber.controller;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.arrowhead.application.skeleton.consumer.ConsumerApplicationInitListener;
import eu.arrowhead.application.skeleton.subscriber.*;
import eu.arrowhead.common.dto.shared.EventDTO;

@RestController
@RequestMapping(SubscriberConstants.DEFAULT_EVENT_NOTIFICATION_BASE_URI)
public class ConsumerController {

	private final Logger logger = LogManager.getLogger(ConsumerController.class);

	@Autowired
	ConsumerApplicationInitListener listener;
	// -------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------

	@PostMapping(path = SubscriberConstants.TEMPERATURE_PROVIDER_START)
	public void receiveEventStartPressureProvider(@RequestBody final EventDTO event) {
		logger.info("Received Temperature provider start event.");

		listener.getTaskRunner().setTemperatureProviderActive(true);
	}

	@PostMapping(path = SubscriberConstants.PRESSURE_PROVIDER_START)
	public void receiveEventStartTemperatureProvider(@RequestBody final EventDTO event) {
		logger.info("Received Pressure provider start event.");

		listener.getTaskRunner().setPressureProviderActive(true);
	}

	@PostMapping(path = SubscriberConstants.TEMPERATURE_PROVIDER_DESTROYED_NOTIFICATION_URI)
	public void receiveEventDestroyedTemperatureProvider(@RequestBody final EventDTO event) {
		logger.info("Received temperature provider destroyed event.");

		listener.getTaskRunner().setTemperatureProviderActive(false);
	}

	@PostMapping(path = SubscriberConstants.PRESSURE_PROVIDER_DESTROYED_NOTIFICATION_URI)
	public void receiveEventDestroyedPressureProvider(@RequestBody final EventDTO event) {
		logger.info("Received pressure provider destroyed event.");

		listener.getTaskRunner().setPressureProviderActive(false);
	}

	// -------------------------------------------------------------------------------------------------
	@PostMapping(path = SubscriberConstants.CRITICAL_TEMPERATURE_NOTIFICATION_URI)
	public void criticalTempEventReceived(@RequestBody final EventDTO event) {
		logger.info("Received critical temperature event with message: {}\nTaking action: Inserting all control rods.",
				event.getPayload());
	}

	// -------------------------------------------------------------------------------------------------
	@PostMapping(path = SubscriberConstants.CRITICAL_PRESSURE_NOTIFICATION_URI)
	public void criticalPressureEventReceived(@RequestBody final EventDTO event) {
		logger.info("Received critical pressure event with message: {}\nTaking action: Opening pressure relief system.",
				event.getPayload());
	}

}
