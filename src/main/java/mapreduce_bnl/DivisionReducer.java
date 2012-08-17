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

		ArrayList<TupleWritable> window = new ArrayList<TupleWritable>(100);
		
		while (values.hasNext()){
			
			boolean pDominatedByWindow = false;
			TupleWritable p = values.next();
			//check tuples in window dominate a point
			for (TupleWritable q : window) {
				if(q.dominate(p))
					pDominatedByWindow = true;
				
				if(p.dominate(q))
					window.remove(q);
					
			}
			
			if (false == pDominatedByWindow)
				window.add(p);
		}
		
		//write local skyline in window to HDFS
		Iterator<TupleWritable> skyline = window.iterator();
		while(skyline.hasNext()){
			output.collect(key, skyline.next());
		}		
	}	
}
