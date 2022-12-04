package ai.aitia.reactor_common.event;

import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

import eu.arrowhead.common.Utilities;

public enum PresetEventType {
	// =================================================================================================
	// elements

	TEMPERATURE_START_INIT(EventTypeConstants.EVENT_TYPE_TEMPERATURE_START_INIT, List.of()),
	PRESSURE_START_INIT(EventTypeConstants.EVENT_TYPE_PRESSURE_START_INIT, List.of()),
	TEMPERATURE_PROVIDER_DESTROYED(EventTypeConstants.EVENT_TYPE_TEMPERATURE_PROVIDER_DESTROYED, List.of()),
	PRESSURE_PROVIDER_DESTROYED(EventTypeConstants.EVENT_TYPE_PRESSURE_PROVIDER_DESTROYED, List.of()),
	CRITICAL_PRESSURE(EventTypeConstants.CRITICAL_PRESSURE, List.of()),
	CRITICAL_TEMPERATURE(EventTypeConstants.CRITICAL_TEMPERATURE, List.of());

	// =================================================================================================
	// members

	private final String eventTypeName;
	private final List<String> metadataKeys;

	// =================================================================================================
	// methods

	// -------------------------------------------------------------------------------------------------
	public String getEventTypeName() {
		return eventTypeName;
	}

	public List<String> getMetadataKeys() {
		return metadataKeys;
	}

	// =================================================================================================
	// assistant methods

	// -------------------------------------------------------------------------------------------------
	private PresetEventType(final String eventTypeName, final List<String> metadataKeys) {
		Assert.isTrue(!Utilities.isEmpty(eventTypeName), "EventType name is invalid.");

		this.metadataKeys = metadataKeys != null ? Collections.unmodifiableList(metadataKeys) : List.of();
		this.eventTypeName = eventTypeName;
	}
}
