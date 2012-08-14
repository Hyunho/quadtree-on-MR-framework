package mapreduce_bnl;

import java.io.IOException;
import java.util.Iterator;

import mapreduce_bnl.io.TupleWritable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class LocalSkylineReducer extends MapReduceBase
	implements Reducer<Text, TupleWritable, Text, TupleWritable>{

	@Override
	public void reduce(Text key, Iterator<TupleWritable> values,
			OutputCollector<Text, TupleWritable> ouput, Reporter reporter)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

}
