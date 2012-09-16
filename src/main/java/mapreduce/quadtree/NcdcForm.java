package mapreduce.quadtree;


public class NcdcForm {

	private static final int MISSING_TEMPERATURE = 9999;
	private static final int MISSING_ATMOSPHERIC_PRESSURE = 99999;

	private String year;
	
	private int airTemperature;
	private String airTemperatureQuality;
	
	final static public int MAX_AIR_TEMPERATURE = 618;
	final static public int MIN_AIR_TEMPERATURE	= -932;			 
	
	final static public int MAX_ATMOSPHERIC_PRESSURE = 10900;
	final static public int MIN_ATMOSPHERIC_pRESSURE = 8600;
	
	public final int minAirTemperature = -932;
	public final int maxAirTemperature = 618;

	private int atmosphericPressure;
	private String atmosphericPressureQuality;
	public final int minAtmosphericPressure = 8600; 
	public final int maxAtmosphericPressure = 10900;
	
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
