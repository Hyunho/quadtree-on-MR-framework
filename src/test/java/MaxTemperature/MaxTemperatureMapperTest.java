package MaxTemperature;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
import java.io.IOException;

import mapreduce.maxTemperature.MaxTemperatureMapper;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.OutputCollector;

import org.junit.*;

public class MaxTemperatureMapperTest {
	
	@Test
	public void processesValidRecord() throws IOException {
		MaxTemperatureMapper mapper = new MaxTemperatureMapper();
		Text value = new Text("004301199099999"+"1950"+"051518004+68750+023550FM-12+0382" +
				"99999V0203201N00261220001CN9999999N9-00111+99999999999");
		
		OutputCollector<Text, IntWritable> output = mock(OutputCollector.class);
		
		mapper.map(null, value, output, null);
		
		verify(output).collect(new Text("1950"), new IntWritable(-11));
	}

	@Test
	public void ignoreMissingTemperatureRecord() throws IOException {
		
		MaxTemperatureMapper mapper = new MaxTemperatureMapper();
		Text value = new Text("004301199099999"+"1950"+"051518004+68750+023550FM-12+0382" +
				                         // year ^^^^     
				"99999V0203201N00261220001CN9999999N9" + "+9999" + "1+99999999999");
                                          // temperature  ^^^^^ 
		OutputCollector<Text, IntWritable> output = mock(OutputCollector.class);
		
		mapper.map(null, value, output, null);

		verify(output, never()).collect(any(Text.class), any(IntWritable.class));
	}
	
}
