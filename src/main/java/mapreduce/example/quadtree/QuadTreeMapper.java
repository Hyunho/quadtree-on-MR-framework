package mapreduce.example.quadtree;

import java.io.IOException;

import mapreduce.io.PointWritable;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import quadtree.Boundary;
import quadtree.Point;
import quadtree.QuadTreeMemory;
import quadtree.Range;



/**
 * get index of tuple using virtual quad-tree
 * @author hyunho
 *
 */
public class QuadTreeMapper extends MapReduceBase 
	implements Mapper<LongWritable, Text, Text, PointWritable> {
		
	private QuadTreeMemory quadTree;

	
	@Override   
	public void configure(JobConf conf) {		
		
		// get boundary		
		String[] strBoundary = conf.getStrings(
				"boundary",				
				new String("0-100"),
				new String("0-100"));		

		Range[] ranges = new Range[strBoundary.length];		
		for(int i=0; i <strBoundary.length; i++ ) {
			ranges[i] = Range.createRange(strBoundary[i], "-");
		}		
		

		Boundary boundary = new Boundary(ranges);
		
		int depth = conf.getInt("depth", 2);
		
		this.quadTree =QuadTreeMemory.getQuadtree(3, boundary, depth);
	}	
	
	@Override
	public void map(LongWritable key, Text iValue,
			OutputCollector<Text, PointWritable> output, Reporter reporter)
			throws IOException {
		

		Point point = new Point(iValue.toString().split(" "));
		

		output.collect(
				new Text("Q" + this.quadTree.getindex(point)),
				new PointWritable(point)
				);
	}
}
