package mapreduce.example.quadtree;

import index.quadtree.Boundary;
import index.quadtree.Point;
import index.quadtree.QuadTree;
import index.quadtree.QuadTreeFile;
import index.quadtree.Range;

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
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;


public class QuadTreeReducer extends MapReduceBase
implements Reducer<Text, PointWritable, Text, Text> {
	
	static class StaticNameMultipleTextOutputFormat 
	extends MultipleTextOutputFormat<Text, Text> {
		
		@Override
		protected String generateFileNameForKeyValue(Text key, Text value,
				String name) {
			return key.toString();
		}
	}

	private int capacity;	

	@Override   
	public void configure(JobConf conf) {		
		//set capacity
		this.capacity = (conf.getInt("capacity", 10));
	}

	@Override
	public void reduce(Text key, Iterator<PointWritable> values,
			OutputCollector<Text, Text> output, Reporter reporter)
	throws IOException {

		
		//build a local quadtree
		QuadTree quadTree = null;
		while(values.hasNext()) {
			
			PointWritable point = values.next();
			
			if (quadTree == null) {
				int dimension = point.point().dimension();
				
				Range[] ranges = new Range[dimension];
				for(int i=0; i< dimension ; i++ ) {
					ranges[i] = new Range(0, 1000);
				}
				
				Boundary boundary = new Boundary(ranges);
				
				quadTree = new QuadTreeFile(
						this.capacity, boundary, key.toString());
			}
			quadTree.insert(point.point());
		}
		
		//output quadtree
		
		//output data of quadtree
		
		//emit points in quadtree
		List<QuadTree> leaves = quadTree.leaves();		
		for(QuadTree qtf : leaves) {			
			Text oKey = new Text(qtf.name());

			Iterator<Point> points = qtf.points();
			
			while(points.hasNext()) {
				Point point = points.next();
				output.collect(
						oKey, new Text(point.toString()));
			}
		}
		
	}
}
