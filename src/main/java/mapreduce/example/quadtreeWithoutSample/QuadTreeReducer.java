package mapreduce.example.quadtreeWithoutSample;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import mapreduce.io.PointWritable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import quadtree.Boundary;
import quadtree.Point;
import quadtree.QuadTreeFile;
import quadtree.Range;

public class QuadTreeReducer extends MapReduceBase
	implements Reducer<Text, PointWritable, Text, Text> {
	
	private int capacity;	
	
	
	private Boundary boundary;
	
	
	
	@Override   
	public void configure(JobConf conf) {		
		//set capacity
		this.capacity = (conf.getInt("capacity", 10));

		// set boundary		
		String[] boundary = conf.getStrings(
				"boundary",				
				new String("0 100"),
				new String("0 100"));		

		Range[] ranges = new Range[boundary.length];		
		for(int i=0; i <boundary.length; i++ ) {
			ranges[i] = Range.createRange(boundary[i], "-");
		}		
		this.boundary = new Boundary(ranges);
	}
	
	@Override
	public void reduce(Text key, Iterator<PointWritable> values,
			OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {
		
		QuadTreeFile quadTree = new QuadTreeFile(
				this.capacity, this.boundary, "Q");
		
		while(values.hasNext()) {
			PointWritable point = values.next();
			quadTree.insert(point.point());
		}
		
		List<QuadTreeFile> descendant = quadTree.descendant();		
		
		for(QuadTreeFile qtf : descendant) {			
			Text oKey = new Text(qtf.name());
			
			if (qtf.isLeaf()) {
				for(Point point : qtf.values()) {
					output.collect(
							oKey, new Text(point.toString()));
				}
			}else {
				output.collect(oKey, new Text(key));
			}
		}
		QuadTreeFile.delete(quadTree.name());

	}
}
