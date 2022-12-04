package eu.arrowhead.application.skeleton.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import eu.arrowhead.application.skeleton.provider.configuration.ConfigConstants;
import eu.arrowhead.common.CommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = { CommonConstants.BASE_PACKAGE, ConfigConstants.BASE_PACKAGE })
public class ProviderMain {

	public static void main(final String[] args) {
		SpringApplication.run(ProviderMain.class, args);
	}

}
