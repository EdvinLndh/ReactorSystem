package ai.aitia.reactor_common;

public class PublisherConstants {
	// =================================================================================================
	// members

	public static final String TEMPERATURE_START_PAYLOAD = "TemperatureStart";
	public static final String PRESSURE_START_PAYLOAD = "PressureStart";
	public static final String TEMPERATURE_PROVIDER_DESTROYED_PAYLOAD = "DestroyStarted";
	public static final String PRESSURE_PROVIDER_DESTROYED_PAYLOAD = "DestroyStarted";
	public static final String CRITICAL_TEMPERATURE_EVENT_PAYLOAD = "Critical temperatures have been reached.";
	public static final String CRITICAL_PRESSURE_EVENT_PAYLOAD = "Critical pressures have been reached.";

	public static final String PRESSURE_ACTION_NONE = "None";
	public static final String PRESSURE_ACTION_OPEN = "Open pressure relief system";

	public static final String GET_ROD_INSERTION_SERVICE_DEFINITION = "get-rod-insertion";
	public static final String GET_ROD_INSERTION_URI = "/rod-insertion";
	public static final String GET_PRESSURE_ACTION_SERVICE_DEFINITION = "get-pressure-action";
	public static final String GET_PRESSURE_ACTION_URI = "/pressure-action";

	// =================================================================================================
	// assistant methods

	// -------------------------------------------------------------------------------------------------
	private PublisherConstants() {
		throw new UnsupportedOperationException();
	}
}
