package index.quadtree;

import java.io.IOException;
import java.net.URI;
import mapreduce.io.PointWritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.util.ReflectionUtils;


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
		Path path = new Path(filename);
		
		SequenceFile.Reader reader = null;
		try {
			reader = new SequenceFile.Reader(fs, path, conf);
			NullWritable key = (NullWritable)
					ReflectionUtils.newInstance(reader.getKeyClass(), conf);
			PointWritable value = (PointWritable)
					ReflectionUtils.newInstance(reader.getValueClass(), conf);

			while(reader.next(key, value)) {
				quadTree.insert(value.point());
			}
		}finally {
			IOUtils.closeStream(reader);
		}
		
	}
}