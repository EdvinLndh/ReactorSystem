package eu.arrowhead.application.skeleton.subscriber;

public class SubscriberConstants {
	public static final String DEFAULT_EVENT_NOTIFICATION_BASE_URI = "/notify";
	public static final String DEFAULT_PRESET_EVENT_TYPES = "#{null}";
	public static final String NOTIFICATION_QUEUE = "notifications";

	public static final String TEMPERATURE_PROVIDER_START = "/" + "tempstart";
	public static final String PRESSURE_PROVIDER_START = "/" + "pressurestart";
	public static final String TEMPERATURE_PROVIDER_DESTROYED_NOTIFICATION_URI = "/" + "tempdestroyed";
	public static final String PRESSURE_PROVIDER_DESTROYED_NOTIFICATION_URI = "/" + "pressuredestroyed";
	public static final String CRITICAL_TEMPERATURE_NOTIFICATION_URI = "/" + "criticaltemperature";
	public static final String CRITICAL_PRESSURE_NOTIFICATION_URI = "/" + "criticalpressure";

	public static final String PRESET_EVENT_TYPES = "preset_events";
	public static final String PUBLISHER_DESTROYED_EVENT_TYPE = "PUBLISHER_DESTROYED";
	public static final String $PRESET_EVENT_TYPES_WD = "${" + PRESET_EVENT_TYPES + ":"
			+ DEFAULT_PRESET_EVENT_TYPES + "}";

	public static final String CONSUMER_TASK = "consumertask";
}