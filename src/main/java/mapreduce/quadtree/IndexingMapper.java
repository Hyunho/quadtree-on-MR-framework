package mapreduce.quadtree;

import java.io.IOException;

import mapreduce.quadtree.io.PointWritable;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
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
public class IndexingMapper extends MapReduceBase 
	implements Mapper<LongWritable, Text, Text, PointWritable> {
		
	private QuadTreeMemory quadTree;
	
	
	/**
	 * generate a quadtree to index a tuple
	 * @param depth
	 */
	public void setQuadtreeDepth(int depth) {		
		Boundary boundary = new Boundary(new Range(0, 100), new Range(0, 100));		
		this.quadTree =QuadTreeMemory.getQuadtree(3, boundary, depth);
		}
	
	
	@Override
	public void map(LongWritable key, Text iValue,
			OutputCollector<Text, PointWritable> output, Reporter reporter)
			throws IOException {
		

		Point point = new Point(iValue.toString().split(" "));
		

		output.collect(
				new Text(this.quadTree.getindex(point)),
				new PointWritable(point)
				);
	}
}
