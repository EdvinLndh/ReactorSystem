package eu.arrowhead.application.skeleton.provider.configuration;

public class ConfigConstants {

	// =================================================================================================
	// members

	public static final int MIN_MAXKEEPALIVE_REQUESTS = 1;
	public static final int MAX_MAXKEEPALIVE_REQUESTS = 1000;
	public static final String HTTP_METHOD = "http-method";
	public static final String INTERFACE_SECURE = "HTTP-SECURE-JSON";
	public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	public static final String GET_LATEST_TEMP_READING_DEFINITION = "get-latest-temp";
	public static final String GET_LATEST_TEMP_URI = "/latest-temp";

	// =================================================================================================
	// assistant methods

	// -------------------------------------------------------------------------------------------------
	private ConfigConstants() {
		throw new UnsupportedOperationException();
	}
}
