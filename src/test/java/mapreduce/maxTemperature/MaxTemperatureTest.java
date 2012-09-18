package mapreduce.maxTemperature;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import mapreduce.maxTemperature.MaxTemperatureMapper;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.OutputCollector;

import org.junit.*;

public class MaxTemperatureTest {
	
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
	
	@Test
	public void returnsMaximumIntegerInValues() throws IOException {
		
		MaxTemperatureReducer reducer = new MaxTemperatureReducer();
		
		Text key = new Text("1950");		
		Iterator<IntWritable> values = Arrays.asList(
				new IntWritable(10), new IntWritable(5)).iterator();
		
		OutputCollector<Text, IntWritable> output = mock(OutputCollector.class);
		
		reducer.reduce(key, values, output, null);		
		verify(output).collect(key, new IntWritable(10));
	}
	
	
}
