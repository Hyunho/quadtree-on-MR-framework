package mapreduce.quadtree;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import mapreduce.quadtree.io.PointWritable;


import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler.IntervalSampler;

import org.junit.*;

import quadtree.Boundary;
import quadtree.Point;
import quadtree.Range;

public class QuadTreeTest {



	@Test 
	public void IndexingMap() throws IOException {		
		
		IndexingMapper indexingMapper = new IndexingMapper();
		indexingMapper.setQuadtreeDepth(2);
		
		Text ivalue = new Text("10 10");

		Point point = new Point(10, 10);
		
		OutputCollector<Text, PointWritable> output =
			mock(OutputCollector.class);
		
		indexingMapper.map(null, ivalue, output, null);
		verify(output).collect(new Text("11"), new PointWritable(point));
	}
	
	@Test
	public void quadtreeReduce() throws IOException  {		

		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));

		QuadTreeReducer reducer= new QuadTreeReducer();
		reducer.setCapacity(3);
		reducer.setBoundary(boundary);

		Text key = new Text("11");
		Iterator<PointWritable> values = Arrays.asList(
				new PointWritable(new Point(10, 10)),
				new PointWritable(new Point(1, 1)),
				new PointWritable(new Point(60, 60)),
				new PointWritable(new Point(20, 20)),
				new PointWritable(new Point(70, 25))).iterator();

		OutputCollector<Text, Text> output =
			mock(OutputCollector.class);

		reducer.reduce(key, values, output, null);		

		verify(output).collect(key, new Text("11"));
		verify(output).collect(key, new Text("111 10.0 10.0"));
		verify(output).collect(key, new Text("111 10.0 10.0"));
		verify(output).collect(key, new Text("114 60.0 60.0"));	
	}
	
	@Test
	public void sampling() throws IOException, InterruptedException {

		Job job = new Job();
		
		job.setInputFormatClass(KeyValueTextInputFormat.class);
			
		String input = "src/test/resources/sample2D-quad.txt";
		
		
		KeyValueTextInputFormat inputFormat = 
			new KeyValueTextInputFormat();

		KeyValueTextInputFormat.setInputPaths(
				job, new Path(input));
	
		
		InputSampler.IntervalSampler<Text, Text> sampler =
			new IntervalSampler<Text, Text>(3,1);
		
		Text[] keys = sampler.getSample(inputFormat, job);
			
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
