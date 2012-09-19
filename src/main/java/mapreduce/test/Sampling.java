package mapreduce.test;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler.IntervalSampler;
import org.junit.Test;

public class Sampling {
	

	@Test
	public static void sampling() throws IOException, InterruptedException {

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
	

	public static void main(String[] args) throws Exception{
		sampling();		
	}
	

}
