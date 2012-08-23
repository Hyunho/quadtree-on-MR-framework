package mapreduce_bnl;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import mapreduce_bnl.io.FlagWritable;
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
		
		DivisionReducer reducer = new DivisionReducer();	
		
		Text key = new Text("00");		
		Iterator<TupleWritable> values = Arrays.asList(
				new TupleWritable(10, 10),
				new TupleWritable(20, 20),
				new TupleWritable(5, 15),
				new TupleWritable(10, 30)).iterator();
		
		OutputCollector<Text, TupleWritable> output = mock(OutputCollector.class);
		
		reducer.reduce(key, values, output, null);		
		verify(output).collect(key, new TupleWritable(10, 10));
		verify(output, never()).collect(key, new TupleWritable(20, 20));
		verify(output).collect(key, new TupleWritable(5, 15));
		verify(output, never()).collect(key, new TupleWritable(10, 30));
	}


	@Test
	public void getherDataInOneReducer() throws IOException {
		
		MergingMapper mapper = new MergingMapper();		
		Text record = new Text("00	6	6");	
		
		OutputCollector<NullWritable, FlagWritable> output = mock(OutputCollector.class);
		
		mapper.map(null, record, output, null);
		
		//key is used as placeholder.
		verify(output).collect(				
				NullWritable.get(),
				new FlagWritable(new Text("00"), new TupleWritable(6, 6)));
	}	
	
	@Test
	public void getGlobalSkyline() throws IOException {
		
		MergingReducer reducer = new MergingReducer();		
		
				
		Iterator<FlagWritable> values = Arrays.asList(
				new FlagWritable(new Text("00"), new TupleWritable(10, 10)),
				new FlagWritable(new Text("11"), new TupleWritable(70, 70)))
				.iterator();
		
		OutputCollector<TupleWritable, NullWritable> output = mock(OutputCollector.class);
		
		reducer.reduce(NullWritable.get(), values, output, null);
		
		verify(output).collect(new TupleWritable(10, 10), NullWritable.get());
		verify(output, never()).collect(new TupleWritable(70, 70), NullWritable.get()); 
		
		
	}

	@Test
	public void dominationTest() throws IOException {
		TupleWritable tuple1 = new TupleWritable(10, 10);
		TupleWritable tuple2 = new TupleWritable(20, 20);
		
		assertTrue(tuple1.dominate(tuple2));
		assertFalse(tuple2.dominate(tuple1));
	}
	
	@Test
	public void compareWritable() throws IOException {
		assertEquals(new TupleWritable(75, 85), new TupleWritable(75,85));
		assertEquals(new Text("11"), new Text("11"));
		assertEquals(new String("11"), new String("11"));
		
		assertEquals(
				new FlagWritable(new Text("11"), new TupleWritable(75, 85)),
				new FlagWritable(new Text("11"), new TupleWritable(75, 85)));
	}
	
	
}

