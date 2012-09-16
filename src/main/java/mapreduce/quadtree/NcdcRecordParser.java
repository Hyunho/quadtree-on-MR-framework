package mapreduce.quadtree;
import org.apache.hadoop.io.Text;


public class NcdcRecordParser {
	
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
		
		ncdcForm.setAtmosphericPressure(Integer.parseInt(record.substring(99,104)));
		ncdcForm.setAtmosphericPressureQuality(record.substring(104,105));
		
		return ncdcForm;
	}
	
	public NcdcForm parse(Text record) {
		return this.parse(record.toString());
	}		
}
