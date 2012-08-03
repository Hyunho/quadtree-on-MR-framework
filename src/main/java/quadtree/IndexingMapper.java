package quadtree;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import quadtree.io.TupleWritableComparable;

public class IndexingMapper extends MapReduceBase 
	implements Mapper<LongWritable, TupleWritableComparable, Text, TupleWritableComparable> {
		
	private int depth = 0;
	
	@Override
	public void map(LongWritable key, TupleWritableComparable value,
			OutputCollector<Text, TupleWritableComparable> output, Reporter reporter)
	throws IOException {
				
		output.collect(
				this.getIndex(value),
				new TupleWritableComparable(value.getAirTemperature(), value.getAtmosphericPressure())
		);
	}	
	
	private Text getIndex(TupleWritableComparable value) {
		
		String temperatureIndex = QuadTreeHelper.getIndex(
				value.getAirTemperature(),
				NcdcForm.MAX_AIR_TEMPERATURE, NcdcForm.MIN_AIR_TEMPERATURE,				 
				depth);
		String atmosphericPressureIndex = QuadTreeHelper.getIndex(
				value.getAtmosphericPressure(),
				NcdcForm.MAX_ATMOSPHERIC_PRESSURE, NcdcForm.MIN_ATMOSPHERIC_pRESSURE, 
				depth);
		
		String allIndex = temperatureIndex + atmosphericPressureIndex;		
		return new Text(allIndex);
	}


	public void setQuadtreeDepth(int depth) {
		this.depth = depth;
	}
	
}
