import java.io.IOException;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;


public class MaxTemperatureMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, Text, IntWritable>{

	public void map(LongWritable key, Text value,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
		// TODO Auto-generated method stub
		
		String line = value.toString();
		String year = line.substring(15,19);
		
		int airTemperature = Integer.parseInt(line.substring(87, 92));
		output.collect(new Text(year), new IntWritable(airTemperature));
		
	}

}
