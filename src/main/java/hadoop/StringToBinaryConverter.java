package hadoop;

import index.quadtree.Point;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import mapreduce.io.PointWritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;

public class StringToBinaryConverter {
	
	
	public static void main(String[] args) throws IOException {
		
		String inputUri = args[0];
		String outputUri = args[1];
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		
		NullWritable key = NullWritable.get();
		PointWritable value = new PointWritable();
		
		DataInputStream dfs = new DataInputStream(fs.open(new Path(inputUri)));
		SequenceFile.Writer writer = null;
		BufferedReader reader = null;

		try {
			writer = SequenceFile.createWriter(fs, conf, new Path(outputUri), key.getClass(), value.getClass());
			reader = new BufferedReader(new InputStreamReader(dfs));

			String line;
			while ((line = reader.readLine()) != null){

				value = new PointWritable(Point.stringToPoint(line));
				writer.append(key, value);
			}
		} finally{
			IOUtils.closeStream(writer);
		}
	}
}
