import static org.junit.Assert.*;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.junit.*;

public class NcdcRecordParserForFormTest {

	@Test
	public void processesValidRecord() throws IOException {

		NcdcRecordParserForForm parser = new NcdcRecordParserForForm();

		Text valid_record = new Text("004301199099999"+"1950"+"051518004+68750+023550FM-12+0382" +
		"99999V0203201N00261220001CN9999999N9"+"-00111"+"+99999"+"102681");

		NcdcForm ncdcForm = parser.parse(valid_record);

		assertEquals(new String("1950"), ncdcForm.getYear());
		assertEquals(-11, ncdcForm.getTemperature());
		assertEquals(10268, ncdcForm.getAtmosphericPressure());		
	}	
	
	@Test
	public void ignoreMissingTemperatureRecord() throws IOException {

		NcdcRecordParserForForm parser = new NcdcRecordParserForForm();		
		
		Text invalid_record = new Text("004301199099999"+"1950"+"051518004+68750+023550FM-12+0382" +
				"99999V0203201N00261220001CN9999999N9" + "+9999" + "1+99999999999");

		NcdcForm ncdcForm = parser.parse(invalid_record);

		assertEquals(false, ncdcForm.isValidTemperature());
		assertEquals(false, ncdcForm.isValidAtmosphricPressure());
	}
}
