package quadtree;

public class NcdcForm {


	private static final int MISSING_TEMPERATURE = 9999;
	private static final int MISSING_ATMOSPHERIC_PRESSURE = 99999;

	private String year;
	private int airTemperature;
	private String airTemperatureQuality;

	private int atmosphericPressure;
	private String atmosphericPressureQuality;

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;		
	}	

	public int getTemperature() {
		return this.airTemperature;
	}

	public void setAirTemperature(int airTemperature) {
		this.airTemperature = airTemperature;
	}


	public void setAirTemperatureQuality(String airTemperatureQuality) {
		this.airTemperatureQuality = airTemperatureQuality;

	}

	public boolean isValidTemperature() {
		return	airTemperature != MISSING_TEMPERATURE &&  
				this.airTemperatureQuality.matches("[01459]");
	}

	public int getAtmosphericPressure() {
		return this.atmosphericPressure;
	}

	public void setAtmosphericPressure(int atomsphericPressure) {
		this.atmosphericPressure = atomsphericPressure;		
	}
	
	public void setAtmosphericPressureQuality(String atomsphericPressureQuality) {
		this.atmosphericPressureQuality = atomsphericPressureQuality;		
	}	

	public boolean isValidAtmosphricPressure() {
		return (this.atmosphericPressure != MISSING_ATMOSPHERIC_PRESSURE) &&
		this.atmosphericPressureQuality.matches("[01459]");
	}
	
	
}
