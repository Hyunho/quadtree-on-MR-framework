package mapreduce_bnl;

import java.io.IOException;

import mapreduce_bnl.io.TupleWritable;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import quadtree.InvertedIndexer;

public class DivisionMapper  extends MapReduceBase
	implements Mapper<LongWritable, Text, Text, TupleWritable>{

	@Override
	public void map(LongWritable key, Text record,
			OutputCollector<Text, TupleWritable> output, Reporter repoter)
			throws IOException {
		
		String[] strings = record.toString().split(" ");
		
		int x = Integer.parseInt(strings[0]);
		int y = Integer.parseInt(strings[1]);
		
		TupleWritable tuple = new TupleWritable(x,y);
		
		String index_x =InvertedIndexer.getIndex(x, 0, 100, 1);
		String index_y = InvertedIndexer.getIndex(y, 0, 100, 1);
		output.collect(
				new Text(index_x+index_y),
				new TupleWritable(x, y)
				);		
	}
}
