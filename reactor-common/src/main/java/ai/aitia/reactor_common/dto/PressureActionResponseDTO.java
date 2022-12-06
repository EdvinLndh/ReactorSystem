package ai.aitia.reactor_common.dto;

import java.io.Serializable;

public class PressureActionResponseDTO implements Serializable {
	private int pressureReading;
	private String pressureScale;
	private String pressureAction;

	public PressureActionResponseDTO() {
	}

	public PressureActionResponseDTO(int pressureReading, String pressureScale) {
		this.pressureReading = pressureReading;
		this.pressureScale = pressureScale;
	}

	public int getPressureReading() {
		return pressureReading;
	}

	public void setPressureReading(int pressureReading) {
		this.pressureReading = pressureReading;
	}

	public String getPressureScale() {
		return pressureScale;
	}

	public void setPressureScale(String pressureScale) {
		this.pressureScale = pressureScale;
	}

	public String getPressureAction() {
		return pressureAction;
	}

	public void setPressureAction(String pressureAction) {
		this.pressureAction = pressureAction;
	}

}