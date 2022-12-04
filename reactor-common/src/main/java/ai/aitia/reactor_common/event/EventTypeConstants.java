package ai.aitia.reactor_common.event;

public class EventTypeConstants {

	// =================================================================================================
	// members

	public static final String EVENT_TYPE_TEMPERATURE_START_INIT = "TEMPERATURE_START_INIT";
	public static final String EVENT_TYPE_PRESSURE_START_INIT = "PRESSURE_START_INIT";
	public static final String EVENT_TYPE_TEMPERATURE_PROVIDER_DESTROYED = "TEMPERATURE_PROVIDER_DESTROYED";
	public static final String EVENT_TYPE_PRESSURE_PROVIDER_DESTROYED = "PRESSURE_PROVIDER_DESTROYED";
	public static final String CRITICAL_TEMPERATURE = "CRITICAL_TEMPERATURE";
	public static final String CRITICAL_PRESSURE = "CRITICAL_PRESSURE";

	// =================================================================================================
	// assistant methods

	// -------------------------------------------------------------------------------------------------
	private EventTypeConstants() {
		throw new UnsupportedOperationException();
	}
}