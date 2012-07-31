import org.apache.hadoop.io.Text;


public class NcdcRecordParserForForm {

	private static final int MISSING_TEMPERATURE = 9999;
	
	private String year;
	private int airTemperature;
	private String quality;
	
	public NcdcForm parse(String record) {
		
		NcdcForm ncdcForm = new NcdcForm();
		
		ncdcForm.setYear(record.substring(15, 19));
		
		String airTemperatureString;
		
		if(record.charAt(87) == '+') {
			airTemperatureString = record.substring(88, 92);				
		} else{
			airTemperatureString = record.substring(87, 92);
		}
		
		ncdcForm.setAirTemperature(Integer.parseInt(airTemperatureString));
		ncdcForm.setAirTemperatureQuality(record.substring(92,93));
		
		return ncdcForm;
	}
	
	public NcdcForm parse(Text record) {
		return this.parse(record.toString());
	}
	
	public boolean isValidTemperature() {
		return airTemperature != MISSING_TEMPERATURE &&  quality.matches("[01459]");
	}	
}
