
public class NcdcForm {

	private String year;
	private int airTemperature;
	private String airTemperatureQuality;
	
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

}
