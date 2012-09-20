package mapreduce.example.quadtreeWithoutSampling;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import mapreduce.example.quadtreeWithoutSample.IndexingMapper;
import mapreduce.example.quadtreeWithoutSample.InvertedIndexer;
import mapreduce.example.quadtreeWithoutSample.QuadTreeReducer;
import mapreduce.io.PointWritable;


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

public class QuadTreeWithoutSampleTest {



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

		QuadTreeReducer reducer= new QuadTreeReducer();
		
		JobConf conf = new JobConf();

		conf.setInt("capacity", 4);
		conf.setStrings("boundary",
				"0-100",
				"0-100"				
		);
		
		reducer.configure(conf);
		
		Text key = new Text("1");
		
		Iterator<PointWritable> values = Arrays.asList(
				new PointWritable(new Point(10, 10)),
				new PointWritable(new Point(1, 1)),
				new PointWritable(new Point(30, 30)),
				new PointWritable(new Point(20, 20)),
				new PointWritable(new Point(30, 15))).iterator();

		OutputCollector<Text, Text> output =
			mock(OutputCollector.class);

		reducer.reduce(key, values, output, null);		

//		verify(output).collect(
//				new Text("Q1"), null);
		verify(output).collect(
				new Text("Q14"), new Text("30.0 30.0"));
		verify(output).collect(
				new Text("Q11"), new Text("10.0 10.0"));
		verify(output).collect(
				new Text("Q11"), new Text("20.0 20.0"));
			
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
