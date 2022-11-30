package ai.aitia.reactor_common.dto;

import java.io.Serializable;

public class RodInsertionResponseDTO implements Serializable {
	private static final long serialVersionUID = -8371510478751740142L;

	private int temperatureReading;
	private String temperatureScale;
	private int pressureReading;
	private String pressureScale;
	private int rodInsertionPrecentage;

	public RodInsertionResponseDTO() {
	}

	public RodInsertionResponseDTO(int temperatureReading, String temperatureScale, int pressureReading,
			String pressureScale, int rodInsertionPrecentage) {
		this.temperatureReading = temperatureReading;
		this.temperatureScale = temperatureScale;
		this.pressureReading = pressureReading;
		this.pressureScale = pressureScale;
		this.rodInsertionPrecentage = rodInsertionPrecentage;
	}

	public RodInsertionResponseDTO(int temperatureReading, int pressureReading, int rodInsertionPrecentage) {
		this.temperatureReading = temperatureReading;
		this.temperatureScale = "celsius";
		this.pressureReading = pressureReading;
		this.pressureScale = "MPa";
		this.rodInsertionPrecentage = rodInsertionPrecentage;
	}

	public int getTemperatureReading() {
		return temperatureReading;
	}

	public void setTemperatureReading(int temperatureReading) {
		this.temperatureReading = temperatureReading;
	}

	public String getTemperatureScale() {
		return temperatureScale;
	}

	public void setTemperatureScale(String temperatureScale) {
		this.temperatureScale = temperatureScale;
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

	public int getRodInsertionPrecentage() {
		return rodInsertionPrecentage;
	}

	public void setRodInsertionPrecentage(int rodInsertionPrecentage) {
		this.rodInsertionPrecentage = rodInsertionPrecentage;
	}

}
