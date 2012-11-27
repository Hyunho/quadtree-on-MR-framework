package mapreduce.example;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import index.quadtree.Point;
import index.quadtree.QuadTreeFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import mapreduce.example.quadtree.InvertedIndexer;
import mapreduce.example.quadtree.QuadTreeReducer;
import mapreduce.example.quadtree.QuadtreeDriverWithoutSample;
import mapreduce.io.PointWritable;

import org.apache.hadoop.io.*;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;


import org.junit.*;




public class QuadTreeTest {

	
	private JobConf conf;

	@Before
	public void before () {
		
		this.conf = new JobConf();
		conf.setInt("capacity", 4);
	
		this.conf.setInt("depth", 2);
	}


	@Test 
	public void IndexingMap() throws IOException {
		
		QuadtreeDriverWithoutSample.QuadTreeMapper indexingMapper = new QuadtreeDriverWithoutSample.QuadTreeMapper();
		indexingMapper.configure(conf);
		
		Text ivalue = new Text("10 10");
		
		
		OutputCollector<Text, PointWritable> output =
			mock(OutputCollector.class);
		
		indexingMapper.map(null, ivalue, output, null);
		verify(output).collect(new Text("Q11"),
				new PointWritable(new Point(10, 10)));
		
		ivalue = new Text("600 600");
		indexingMapper.map(null, ivalue, output, null);
		verify(output).collect(new Text("Q41"),
				new PointWritable(new Point(600, 600)));
	}	
	
	@Test	
	public void testIndexing() throws IOException {
		int value = 20;		
		int max = 100;
		int min = 0;		
		int depth = 3;

		assertEquals("001", InvertedIndexer.getIndex(value, max, min, depth));
	}
	
	@After
	public void deleteFiles() {
		QuadTreeFile.delete("1");
		QuadTreeFile.delete("Q");

	}
	
}
