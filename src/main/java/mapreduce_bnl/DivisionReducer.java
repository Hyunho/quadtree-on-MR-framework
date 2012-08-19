package mapreduce_bnl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import mapreduce_bnl.io.TupleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import com.sun.tools.javac.util.List;

public class DivisionReducer extends MapReduceBase
	implements Reducer<Text, TupleWritable, Text, TupleWritable>{

	/**
	 * using BNL, We get local skyline.
	 * In origin, if skyline size is larger thatn window size, needs a disk I/O.
	 * But I don't support it yet. 
	 */
	@Override
	public void reduce(Text key, Iterator<TupleWritable> values,
			OutputCollector<Text, TupleWritable> output, Reporter reporter)
			throws IOException {
		
		ArrayList<TupleWritable> skyline = Skyline.getSkylineUsingBNL(values);
		
		//write local skyline in window to HDFS
		Iterator<TupleWritable> skylineIterator = skyline.iterator();
		while(skylineIterator.hasNext()){
			output.collect(key, skylineIterator.next());
		}		
	}
}