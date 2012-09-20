package mapreduce.example.quadtreeWithoutSample;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import mapreduce.io.PointWritable;

import org.apache.hadoop.io.Text;
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
	
	private int capacity = 10000;
	
	public void setCapacity(int capacity)  {
		this.capacity = capacity;
	}
	
	private Boundary boundary;
	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;	}

	
	public QuadTreeReducer() {
		Boundary boundary = new Boundary(
				new Range(0, 100), new Range(0, 100));
		this.setCapacity(3);
		this.setBoundary(boundary);

	}
	@Override
	public void reduce(Text key, Iterator<PointWritable> values,
			OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {
		
		QuadTreeFile quadTree = new QuadTreeFile(
				this.capacity, this.boundary, key.toString());
		
		while(values.hasNext()) {
			PointWritable point = values.next();
			quadTree.insert(point.point());
		}
		
		List<QuadTreeFile> descendant = quadTree.descendant();		
		
		for(QuadTreeFile qtf : descendant) {			
			String str = new String();
			str += qtf.name();
			
			if (qtf.isLeaf()) {
				for(Point point : qtf.values()) {
					output.collect(key,
							new Text(str + " " + point.toString()));
				}
			}else {
				output.collect(key, new Text(str));
			}
		}
		QuadTreeFile.delete(quadTree.name());

	}
}
