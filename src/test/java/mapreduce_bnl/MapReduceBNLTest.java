package mapreduce_bnl;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import mapreduce_bnl.io.TupleWritable;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.OutputCollector;

import org.junit.*;


public class MapReduceBNLTest {

	@Test
	public void divideData() throws IOException {
		DivisionMapper mapper = new DivisionMapper();
		
		Text record = new Text("75 85");	
		
		
		OutputCollector<Text, TupleWritable> output = mock(OutputCollector.class);		
		
		
		mapper.map(null, record, output, null);
		
		verify(output).collect(
				new Text("11"),
				new TupleWritable(75, 85));		
	}
	
	@Test
	public void getLocalSkylineAndWriteIt() throws IOException {
		
		LocalSkylineReducer reducer = new LocalSkylineReducer();
		
		
		Text key = new Text("00");		
		Iterator<TupleWritable> values = Arrays.asList(
				new TupleWritable(10, 10),
				new TupleWritable(20, 20))
				.iterator();
		
		OutputCollector<Text, TupleWritable> output = mock(OutputCollector.class);
		
		reducer.reduce(key, values, output, null);		
		verify(output).collect(key, new TupleWritable(10, 10));
		verify(output, never()).collect(key, new TupleWritable(20, 20)); 
	}




	@Test
	public void getherDataInOneReducer() throws IOException {
		
		Text record = new Text("75 85");
		
		
	}
	
	@Test
	public void getGlobalSkyline() throws IOException {
		
	}
	
	
}

