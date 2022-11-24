package eu.arrowhead.application.skeleton.subscriber;

public class SubscriberConstants {
	public static final String DEFAULT_EVENT_NOTIFICATION_BASE_URI = "/notify";
	public static final String DEFAULT_PRESET_EVENT_TYPES = "#{null}";
	public static final String NOTIFICATION_QUEUE = "notifications";
	public static final String PUBLISHER_DESTORYED_NOTIFICATION_URI = "/" + "publisherdestroyed";
	public static final String REQUEST_RECEIVED_NOTIFICATION_URI = "/" + "requestreceived";
	public static final String CRITICAL_TEMPERATURE_NOTIFICATION_URI = "/" + "criticaltemperature";
	public static final String PRESET_EVENT_TYPES = "preset_events";
	public static final String PUBLISHER_DESTROYED_EVENT_TYPE = "PUBLISHER_DESTROYED";
	public static final String REQUEST_RECEIVED_EVENT_TYPE = "REQUEST_RECEIVED";
	public static final String $PRESET_EVENT_TYPES_WD = "${" + PRESET_EVENT_TYPES + ":"
			+ DEFAULT_PRESET_EVENT_TYPES + "}";

	public static final String CONSUMER_TASK = "consumertask";
}