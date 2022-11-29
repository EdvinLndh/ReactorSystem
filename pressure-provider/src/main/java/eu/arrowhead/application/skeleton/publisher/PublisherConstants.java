package eu.arrowhead.application.skeleton.publisher;

public class PublisherConstants {
	// =================================================================================================
	// members

	public static final String START_INIT_EVENT_PAYLOAD = "InitStarted";
	public static final String START_RUN_EVENT_PAYLOAD = "RunStarted";
	public static final String PUBLISHR_DESTROYED_EVENT_PAYLOAD = "DestroyStarted";
	public static final String CRITICAL_TEMPERATURE_EVENT_PAYLOAD = "Critical temperatures have been reached.";

	// =================================================================================================
	// assistant methods

	// -------------------------------------------------------------------------------------------------
	private PublisherConstants() {
		throw new UnsupportedOperationException();
	}
}
