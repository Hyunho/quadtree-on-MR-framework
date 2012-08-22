package mapreduce_bnl;

import java.io.IOException;

import mapreduce_bnl.io.FlagWritable;
import mapreduce_bnl.io.TupleWritable;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class MergingMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, NullWritable, FlagWritable>{

	@Override
	public void map(LongWritable key, Text record,
			OutputCollector<NullWritable, FlagWritable> output, Reporter reporter)
			throws IOException {
		
		String[] strings = record.toString().split("\t");
		
		Text flag = new Text(strings[0]);
		int x = Integer.parseInt(strings[1]);
		int y = Integer.parseInt(strings[2]);
		TupleWritable tuple = new TupleWritable(x, y);		
				
		output.collect(
				NullWritable.get(),
				new FlagWritable(flag, tuple));
	}
}
