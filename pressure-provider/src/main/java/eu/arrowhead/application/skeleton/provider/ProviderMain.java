package eu.arrowhead.application.skeleton.provider;

import java.util.Map;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import ai.aitia.reactor_common.PublisherConstants;
import ai.aitia.reactor_common.event.PresetEventType;
import ai.aitia.reactor_common.service.PublisherService;
import eu.arrowhead.application.skeleton.provider.configuration.ConfigConstants;
import eu.arrowhead.common.CommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = { CommonConstants.BASE_PACKAGE, ConfigConstants.BASE_PACKAGE })
public class ProviderMain implements ApplicationRunner {

	@Autowired
	private PublisherService publisherService;

	private final Logger logger = LogManager.getLogger(ProviderMain.class);

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------
	public static void main(final String[] args) {
		SpringApplication.run(ProviderMain.class, args);
	}

	@Override
	public void run(final ApplicationArguments args) throws Exception {
		logger.debug("run started...");
		publishRunStartedEvent();
	}

	// =================================================================================================
	// assistant methods

	// -------------------------------------------------------------------------------------------------
	// Sample implementation of event publishing when application run started
	private void publishRunStartedEvent() {
		logger.debug("publishRunStartedEvent started...");

		final Map<String, String> metadata = null;
		publisherService.publish(PresetEventType.START_RUN, metadata, PublisherConstants.START_RUN_EVENT_PAYLOAD);

	}
}
