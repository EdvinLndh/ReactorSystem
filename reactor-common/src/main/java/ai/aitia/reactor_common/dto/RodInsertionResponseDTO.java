package ai.aitia.reactor_common.dto;

import java.io.Serializable;

public class RodInsertionResponseDTO implements Serializable {
	private static final long serialVersionUID = -8371510478751740142L;

	private int temperatureReading;
	private String temperatureScale;
	private int rodInsertionPrecentage;

	public RodInsertionResponseDTO() {
	}

	public RodInsertionResponseDTO(int temperatureReading, String temperatureScale, int rodInsertionPrecentage) {
		this.temperatureReading = temperatureReading;
		this.temperatureScale = temperatureScale;
		this.rodInsertionPrecentage = rodInsertionPrecentage;
	}

	public RodInsertionResponseDTO(int temperatureReading, int rodInsertionPrecentage) {
		this.temperatureReading = temperatureReading;
		this.temperatureScale = "celsius";
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

	public int getRodInsertionPrecentage() {
		return rodInsertionPrecentage;
	}

	public void setRodInsertionPrecentage(int rodInsertionPrecentage) {
		this.rodInsertionPrecentage = rodInsertionPrecentage;
	}

}
