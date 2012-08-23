package mapreduce_bnl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import mapreduce_bnl.io.FlagWritable;
import mapreduce_bnl.io.TupleWritable;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class MergingReducer extends MapReduceBase 
	implements Reducer<NullWritable, FlagWritable, TupleWritable, NullWritable>{

	@Override
	public void reduce(NullWritable nil, Iterator<FlagWritable> flags,
			OutputCollector<TupleWritable, NullWritable> output, Reporter repoter)
			throws IOException {
		
		
		
		//get only tuples
		ArrayList<TupleWritable> tuples = new ArrayList<TupleWritable>();
		
		while(flags.hasNext()) {
			TupleWritable tuple = flags.next().getTuple();			
			tuples.add(new TupleWritable(tuple.getX(), tuple.getY()));
		}
		
		
		ArrayList<TupleWritable> skyline = new SkylineHelper(tuples.iterator()).getSkylineUsingBNL();
		
		for (TupleWritable tupleWritable : skyline) {
			output.collect(tupleWritable, NullWritable.get());
		}		
	}
}
