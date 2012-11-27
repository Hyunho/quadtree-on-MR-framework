package mapreduce.example.quadtree;

import java.io.ObjectOutputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import index.quadtree.Boundary;
import index.quadtree.Point;
import index.quadtree.QuadTree;
import index.quadtree.QuadTreeFile;
import index.quadtree.Range;
import hadoop.Sampler;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;

public class QuadtreeDriver {

	private static String dst = "quadtree.dat";

	public static void main(String[] args) throws Exception{

		int numSample = 10000;
		
		int capacityOfBaseQuadtree = numSample / 16; 

		if(args.length != 2) {
			System.err.printf("Usage: %s [generic option] <input> <output>\n", 
					QuadTreeDriverWithSample.class.getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			System.exit(-1);
		}
		
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		//sampling
		System.out.println("start sampling");
		System.out.println(dateFormat.format(cal.getTime()));

		Point[] points = Sampler.reservoirSampling(args[0], numSample);

		int dimension = points[0].dimension();
		
		Range[] ranges = new Range[dimension];
		for(int i=0; i< dimension ; i++ ) {
			ranges[i] = new Range(0, 1000);
		}
		
		// initialize a base quadtree
		QuadTree quadTree = new QuadTreeFile(
				capacityOfBaseQuadtree, 
				new Boundary(ranges),
				"Q");				
		
		// build a quadtree
		System.out.println("build a quadtree");
		cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));

		int i=0;
		while(i<numSample) {
			quadTree.insert(points[i]);
			i++;
		}
		
		
		
		//write quadtree in to HDFS
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dst), conf);
		FSDataOutputStream os = fs.create(new Path(dst), true);				
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(quadTree);
		oos.close();
		
		
		ToolRunner.run(new QuadTreeDriverWithSample.BuldingQuadtree(), args);

		System.exit(0);
	}
}
