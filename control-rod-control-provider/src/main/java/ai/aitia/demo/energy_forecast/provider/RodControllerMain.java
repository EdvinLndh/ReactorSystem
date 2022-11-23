package ai.aitia.demo.energy_forecast.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import eu.arrowhead.common.CommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = { CommonConstants.BASE_PACKAGE, "ai.aitia" })
public class RodControllerMain {

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------
	public static void main(final String[] args) {
		SpringApplication.run(RodControllerMain.class, args);
	}
}
