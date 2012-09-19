package mapreduce.quadtree;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import mapreduce.quadtree.*;
import mapreduce.quadtree.io.PointWritable;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.OutputCollector;
import org.junit.*;

import quadtree.Boundary;
import quadtree.Point;
import quadtree.Range;

public class QuadTreeTest {



	@Test 
	public void IndexingMap() throws IOException {		
		
		IndexingMapper indexingMapper = new IndexingMapper();
		indexingMapper.setQuadtreeDepth(2);
		
		
		Point point = new Point(10, 10);
		
		OutputCollector<Text, PointWritable> output =
			mock(OutputCollector.class);
		
		indexingMapper.map(null, new PointWritable(point), output, null);
		verify(output).collect(new Text("11"), new PointWritable(point));
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
