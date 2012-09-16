package mapreduce.maxTemperature;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.*;

import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class MaxTemperatureReducer extends MapReduceBase
	implements Reducer<Text, IntWritable, Text, IntWritable> {

	@Override
	public void reduce(Text key, Iterator<IntWritable> values,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
		
		int maxValue = Integer.MIN_VALUE;
		while(values.hasNext()) {
			maxValue = Math.max(maxValue, values.next().get());
		}
		
		output.collect(key, new IntWritable(maxValue));
	}
}
