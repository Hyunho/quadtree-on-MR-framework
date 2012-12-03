package index.quadtree;

import hadoop.io.PointInputStream;

import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


public class QuadtreeBuilder {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.println("there is no input arg[0]");
			System.exit(-1);
		}

		String fileName = args[0];
		buildingWithSingleMachine(fileName);

	}

	private static void buildingWithSingleMachine(String filename) throws IOException {

		QuadTreeFile quadTree = new QuadTreeFile(10000,
				new Boundary(new Range(0, 1000), new Range(0, 1000)), "Q");
		
		Configuration conf = new Configuration();		
		FileSystem fs = FileSystem.get(URI.create(filename), conf);		
		PointInputStream dis = new PointInputStream(fs.open(new Path(filename)), 2);		

		try {
			Point point;
			while((point = dis.readPoint()) != null) {
				quadTree.insert(point);
			}
			quadTree.save();
		}finally {
			dis.read();
		}
		
	}
}