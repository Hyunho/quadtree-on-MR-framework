package mapreduce.example.quadtree;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import mapreduce.example.quadtreeWithSampling.SampleMapper;
import mapreduce.example.quadtreeWithoutSample.QuadTreeReducer;
import mapreduce.io.PointWritable;
import mapreduce.io.TupleWritable;

import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.junit.Test;

import quadtree.Boundary;
import quadtree.Point;
import quadtree.Range;

public class SamplingTest {
	
	//@Test
	public void sampling() throws IOException {
		
		SampleMapper mapper = new SampleMapper();
		
		mapper.setInterval(2);
		
		List<Text> values = Arrays.asList(
				new Text("10 10"),
				new Text("20 20"),
				new Text("30 30"),
				new Text("40 40"));
		
		OutputCollector<NullWritable, Text> output =
			mock(OutputCollector.class);
		
		for(Text value: values) {
			mapper.map(NullWritable.get(), value, output, null);
		}	
		
		verify(output).collect(NullWritable.get(), new Text("20 20"));	
		verify(output, never()).collect(NullWritable.get(), new Text("10 10"));		
	}
}
