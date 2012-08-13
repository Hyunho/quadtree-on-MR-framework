package mapreduce_bnl;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.OutputCollector;

import org.junit.*;


public class MapReduceBNLTest {


	@Test
	public void divideData() throws IOException {
		DivisionMapper mapper = nwe DivisionMapper();
		
		Text record = new Text("0.100441153460505 0.128266280946252");	
		
		
		OutputCollector<Text, IntWritable> output = mock(OutputCollector.class);		
		
		
		mapper.map(null, value, output, null);
		
		verify(output).collect(
				new Text("11"),
				new PointWritable(0.100441153460505, 0.128266280946252));		
	}
	
	@Test
	public void getLocalSkylineAndWriteIt() throws IOException {
		
		LocalSkylineReduce reduce = new LocalSkylineReducer()
		
		
		Text key = new Text("11");		
		Iterator<IntWritable> values = Arrays.asList(
				new PointWritable(10, 10),
				new PointWritable(20, 20))
				.iterator();
		
		OutputCollector<Text, IntWritable> output = mock(OutputCollector.class);
		
		reducer.reduce(key, values, output, null);		
		verify(output).collect(key, new PointWritable(10, 10));
	}


	@Test
	public void getherDataInOneReducer() throws IOException {
		
	
	}
	
	@Test
	public void getGlobalSkyline() throws IOException {
		
	}
	
	
}

