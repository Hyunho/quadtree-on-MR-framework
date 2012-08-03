package quadtree;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import quadtree.io.TupleWritableComparable;

public class ParsingMapper extends MapReduceBase 
implements Mapper<LongWritable, Text, Text, TupleWritableComparable> {


	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<Text, TupleWritableComparable> output, Reporter reporter)
	throws IOException {

		NcdcRecordParser parser = new NcdcRecordParser();		
		NcdcForm form = parser.parse(value);

		output.collect(
				null,
				new TupleWritableComparable(form.getTemperature(), form.getAtmosphericPressure())
		);
	}		
}