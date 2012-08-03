package quadtree;

import java.io.IOException;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import quadtree.io.TupleWritableComparable;

public class QuadTreeMapper extends MapReduceBase
implements Mapper<LongWritable, Text, Text, TupleWritableComparable> {

	private NcdcRecordParser parser = new NcdcRecordParser();
	private int depth = 0;

	public void map(LongWritable key, Text value,
			OutputCollector<Text, TupleWritableComparable> output, Reporter reporter)
			throws IOException {
		
		NcdcForm form = parser.parse(value);

		if(form.isValidTemperature() && form.isValidTemperature()) {			
			String index = getIndex(form, this.depth);
			
			output.collect(
					new Text(index),
					new TupleWritableComparable(form.getTemperature(), form.getAtmosphericPressure())
					);
		}		
	}

	public void setQuadtreeDepth(int depth) {
		this.depth = depth;
	}
	
	public String getIndex(NcdcForm form, int depth)
	{
		String temperatureIndex = QuadTreeHelper.getIndex(
				form.getTemperature(),
				form.maxAirTemperature, form.minAirTemperature,				 
				depth);
		String atmosphericPressureIndex = QuadTreeHelper.getIndex(
				form.getAtmosphericPressure(),
				form.maxAtmosphericPressure, form.minAtmosphericPressure, 
				depth);
		
		String allIndex = temperatureIndex + atmosphericPressureIndex;		
		return allIndex;
	}
}