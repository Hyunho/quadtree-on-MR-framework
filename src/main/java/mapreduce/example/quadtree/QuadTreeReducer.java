package mapreduce.example.quadtree;

import index.quadtree.Boundary;
import index.quadtree.Point;
import index.quadtree.QuadTree;
import index.quadtree.QuadTreeFile;
import index.quadtree.Range;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import mapreduce.io.PointWritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;


public class QuadTreeReducer extends MapReduceBase
implements Reducer<Text, PointWritable, Text, Text> {
	
	

	private int capacity;
	private QuadTreeFile baseQuadtree;	

	@Override   
	public void configure(JobConf conf) {		
		//set capacity
		this.capacity = (conf.getInt("capacity", 10));
		
		try {
			FileSystem fs = FileSystem.get(conf);
			FSDataInputStream fis=  fs.open(new Path("quadtree.dat"));
			ObjectInputStream os = new ObjectInputStream(fis);
			this.baseQuadtree = (QuadTreeFile)os.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
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
				QuadTreeFile node = baseQuadtree.getNode(key.toString());				
				quadTree = new QuadTreeFile(
						this.capacity, node.boundary(), key.toString());
			}
			quadTree.insert(point.point());
		}
		
		//output quadtree
		//write quadtree in to HDFS
		String dst = key.toString();
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dst), conf);
		FSDataOutputStream os = fs.create(new Path(dst), true);				
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(quadTree);
	
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
