package mapreduce.example.quadtree;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;


public class SampleMapper extends MapReduceBase 
	implements Mapper<NullWritable, Text, NullWritable, Text>{

	


	private int interval;
	private int count = 0;
	
	public void setInterval(int interval) {
		this.interval = interval;
		
	}

	@Override
	public void map(NullWritable ikey, Text ivalue,
			OutputCollector<NullWritable, Text> output, Reporter reporter)
			throws IOException {
		count++;
		
		if(count%2 == 0) {
			output.collect(NullWritable.get(), ivalue);
		}
	}


}
