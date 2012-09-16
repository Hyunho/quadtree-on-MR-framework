package quadtree;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import mapreduce.quadtree.*;
import mapreduce.quadtree.io.TupleWritableComparable;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.OutputCollector;
import org.junit.*;

public class QuadTreeTest {


	@Test
	public void testParsingMap() throws IOException {		
		ParsingMapper parsingMapper = new ParsingMapper();
		
		// temperature : -11
		// atmosphericPressure : 10268
		Text record = new Text("004301199099999"+"1950"+"051518004+68750+023550FM-12+0382" +
				"99999V0203201N00261220001CN9999999N9"+"-00111"+"+99999"+"102681");

		OutputCollector<Text, TupleWritableComparable> output = mock(OutputCollector.class);		
		parsingMapper.map(null, record, output, null);
		verify(output).collect(null, new TupleWritableComparable(-11, 10268));
	}
	
	@Test 
	public void testIndexingMap() throws IOException {
		IndexingMapper indexingMapper = new IndexingMapper();
		
		int airTemperature = -11;
		int atmosphericPressure = 10268;
		
		//mean of temperature = (max(618) + min(-932)) / 2 = -243 
		//mean of atmosphericPressure = (max(10900) + min(8600) / 2 = 9750
		
		TupleWritableComparable tuple =  new TupleWritableComparable(
				airTemperature,
				atmosphericPressure);
		
		OutputCollector<Text, TupleWritableComparable> output = mock(OutputCollector.class);
		indexingMapper.setQuadtreeDepth(1);	

		indexingMapper.map(null, tuple, output, null);
		verify(output).collect(new Text("11"), new TupleWritableComparable(-11, 10268));
		verify(output, never()).collect(new Text("11"), new TupleWritableComparable(-10, 10268));
	}
	
	@Test
	public void returnsMaximumIntegerInValues() throws IOException {
		
//	MaxTemperatureReducer reducer = new MaxTemperatureReducer();
//		
//		Text key = new Text("1950");		
//		Iterator<IntWritable> values = Arrays.asList(
//				new IntWritable(10), new IntWritable(5)).iterator();
//		
//		OutputCollector<Text, IntWritable> output = mock(OutputCollector.class);
//		
//		reducer.reduce(key, values, output, null);		
//		verify(output).collect(key, new IntWritable(10));
//		
//		QuadTreeReducer reducer = new QuadTreeReducer();
//		
//		Text index = new Text("00");		
//		Iterator<TupleWritableComparable> values = Arrays.asList(
//				new TupleWritableComparable(10, 10),
//				new TupleWritableComparable(20, 20),
//				new TupleWritableComparable(30, 30))
//				.iterator();
//		
//		OutputCollector<Text, TupleArrayWritable> nullOutput = mock(OutputCollector.class);
//		
//		reducer.setThreshold(5);
//		reducer.reduce(index, values, nullOutput, null);		
//		verify(nullOutput).collect(null, null);
//		
//		OutputCollector<Text, TupleArrayWritable> reOutput = mock(OutputCollector.class);
//		
//		reducer.setThreshold(2);
//		reducer.reduce(index, values, reOutput, null);		
//		verify(reOutput).collect(index, values);
		
	}

	
	@Test	
	public void testIndexing() throws IOException {
		int value = 20;		
		int max = 100;
		int min = 0;		
		int depth = 3;

		assertEquals("001", InvertedIndexer.getIndex(value, max, min, depth));
	}
}
