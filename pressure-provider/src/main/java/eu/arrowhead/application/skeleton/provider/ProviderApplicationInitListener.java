package eu.arrowhead.application.skeleton.provider;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import ai.aitia.arrowhead.application.library.config.ApplicationInitListener;
import ai.aitia.arrowhead.application.library.util.ApplicationCommonConstants;
import ai.aitia.reactor_common.PublisherConstants;
import ai.aitia.reactor_common.event.PresetEventType;
import ai.aitia.reactor_common.service.PublisherService;
import eu.arrowhead.application.skeleton.provider.configuration.ConfigConstants;
import eu.arrowhead.application.skeleton.publisher.security.PublisherSecurityConfig;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.core.CoreSystem;
import eu.arrowhead.common.dto.shared.ServiceRegistryRequestDTO;
import eu.arrowhead.common.dto.shared.ServiceSecurityType;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;
import eu.arrowhead.common.exception.ArrowheadException;

@Component
public class ProviderApplicationInitListener extends ApplicationInitListener {

	// =================================================================================================
	// members

	@Autowired
	private ArrowheadService arrowheadService;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	private PublisherSecurityConfig publisherSecurityConfig;

	@Autowired
	private PublisherService publisherService;

	@Value(ApplicationCommonConstants.$TOKEN_SECURITY_FILTER_ENABLED_WD)
	private boolean tokenSecurityFilterEnabled;

	@Value(CommonConstants.$SERVER_SSL_ENABLED_WD)
	private boolean sslEnabled;

	@Value(ApplicationCommonConstants.$APPLICATION_SYSTEM_NAME)
	private String mySystemName;

	@Value(ApplicationCommonConstants.$APPLICATION_SERVER_ADDRESS_WD)
	private String mySystemAddress;

	@Value(ApplicationCommonConstants.$APPLICATION_SERVER_PORT_WD)
	private int mySystemPort;

	private final Logger logger = LogManager.getLogger(ProviderApplicationInitListener.class);

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------
	@Override
	protected void customInit(final ContextRefreshedEvent event) {
		checkConfiguration();

		// Checking the availability of necessary core systems
		checkCoreSystemReachability(CoreSystem.SERVICEREGISTRY);
		if (sslEnabled && tokenSecurityFilterEnabled) {
			checkCoreSystemReachability(CoreSystem.AUTHORIZATION);

			// Initialize Arrowhead Context
			arrowheadService.updateCoreServiceURIs(CoreSystem.AUTHORIZATION);

			setTokenSecurityFilter();

		} else {
			logger.info("TokenSecurityFilter in not active");
		}

		if (arrowheadService.echoCoreSystem(CoreSystem.EVENTHANDLER)) {
			arrowheadService.updateCoreServiceURIs(CoreSystem.EVENTHANDLER);
			publishInitStartedEvent();
		}

		final ServiceRegistryRequestDTO pressureService = createServiceRegistryRequest(
				ConfigConstants.GET_LATEST_PRESSURE_READING_DEFINITION, ConfigConstants.GET_LATEST_PRESSURE_URI,
				HttpMethod.GET);
		arrowheadService.forceRegisterServiceToServiceRegistry(pressureService);
		logger.info("Service registered: {}", ConfigConstants.GET_LATEST_PRESSURE_READING_DEFINITION);

		PressureGenerator pressureGenerator = new PressureGenerator();
		applicationContext.getAutowireCapableBeanFactory().autowireBean(pressureGenerator);
		pressureGenerator.start();

	}

	private ServiceRegistryRequestDTO createServiceRegistryRequest(String serviceDefinition,
			String serviceUri, HttpMethod httpMethod) {
		final ServiceRegistryRequestDTO serviceRegistryRequest = new ServiceRegistryRequestDTO();
		serviceRegistryRequest.setServiceDefinition(serviceDefinition);
		final SystemRequestDTO systemRequest = new SystemRequestDTO();
		systemRequest.setSystemName(mySystemName);
		systemRequest.setAddress(mySystemAddress);
		systemRequest.setPort(mySystemPort);

		if (sslEnabled && tokenSecurityFilterEnabled) {
			systemRequest.setAuthenticationInfo(
					Base64.getEncoder().encodeToString(arrowheadService.getMyPublicKey().getEncoded()));
			serviceRegistryRequest.setSecure(ServiceSecurityType.TOKEN.name());
			serviceRegistryRequest.setInterfaces(List.of(ConfigConstants.INTERFACE_SECURE));
		} else if (sslEnabled) {
			systemRequest.setAuthenticationInfo(
					Base64.getEncoder().encodeToString(arrowheadService.getMyPublicKey().getEncoded()));
			serviceRegistryRequest.setSecure(ServiceSecurityType.CERTIFICATE.name());
			serviceRegistryRequest.setInterfaces(List.of(ConfigConstants.INTERFACE_SECURE));
		} else {
			serviceRegistryRequest.setSecure(ServiceSecurityType.NOT_SECURE.name());
			serviceRegistryRequest.setInterfaces(List.of(ConfigConstants.INTERFACE_INSECURE));
		}
		serviceRegistryRequest.setProviderSystem(systemRequest);
		serviceRegistryRequest.setServiceUri(serviceUri);
		serviceRegistryRequest.setMetadata(new HashMap<>());
		serviceRegistryRequest.getMetadata().put(ConfigConstants.HTTP_METHOD, httpMethod.name());
		return serviceRegistryRequest;
	}

	// -------------------------------------------------------------------------------------------------
	@Override
	public void customDestroy() {
		arrowheadService.unregisterServiceFromServiceRegistry(ConfigConstants.GET_LATEST_PRESSURE_READING_DEFINITION,
				ConfigConstants.GET_LATEST_PRESSURE_URI);
		logger.info("Service unregistered: {}", ConfigConstants.GET_LATEST_PRESSURE_READING_DEFINITION);
		publishDestroyedEvent();
	}

	// =================================================================================================
	// assistant methods

	// -------------------------------------------------------------------------------------------------
	private void checkConfiguration() {
		if (!sslEnabled && tokenSecurityFilterEnabled) {
			logger.warn("Contradictory configuration:");
			logger.warn("token.security.filter.enabled=true while server.ssl.enabled=false");
		}
	}

	// -------------------------------------------------------------------------------------------------
	private void setTokenSecurityFilter() {
		final PublicKey authorizationPublicKey = arrowheadService.queryAuthorizationPublicKey();
		if (authorizationPublicKey == null) {
			throw new ArrowheadException("Authorization public key is null");
		}

		KeyStore keystore;
		try {
			keystore = KeyStore.getInstance(sslProperties.getKeyStoreType());
			keystore.load(sslProperties.getKeyStore().getInputStream(),
					sslProperties.getKeyStorePassword().toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException ex) {
			throw new ArrowheadException(ex.getMessage());
		}
		final PrivateKey providerPrivateKey = Utilities.getPrivateKey(keystore, sslProperties.getKeyPassword());

		publisherSecurityConfig.getTokenSecurityFilter().setAuthorizationPublicKey(authorizationPublicKey);
		publisherSecurityConfig.getTokenSecurityFilter().setMyPrivateKey(providerPrivateKey);

	}

	private void publishDestroyedEvent() {
		logger.debug("publishdestroyevent started...");
		publisherService.publish(PresetEventType.PRESSURE_PROVIDER_DESTROYED, (Map<String, String>) null,
				PublisherConstants.PRESSURE_PROVIDER_DESTROYED_PAYLOAD);

	}

	// Sample implementation of event publishing at application init time
	private void publishInitStartedEvent() {
		logger.debug("publishInitStartedEvent started...");

		publisherService.publish(PresetEventType.PRESSURE_START_INIT, (Map<String, String>) null,
				PublisherConstants.PRESSURE_START_PAYLOAD);
	}

}
