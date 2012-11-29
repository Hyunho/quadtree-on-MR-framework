package hadoop;


import java.io.IOException;
import java.net.URI;
import java.util.Random;

import mapreduce.io.PointWritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.util.ReflectionUtils;

import index.quadtree.Point;

public class Sampler {
	
	
	public static Point[] reservoirSampling(String filename, int numSample) throws IOException {
		
		Point[] points = new Point[numSample];
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

			int index = 0;
			Random rand = new Random();
			
			while(reader.next(key)) {
				
				if(index< numSample) {
					reader.getCurrentValue(value);
					points[index] = value.point();
				}else {
					int r = rand.nextInt(index);
					if(r<numSample) {						
						reader.getCurrentValue(value);
						points[r] = value.point();
					}
				}
				index++;				
			}
		}finally {
			IOUtils.closeStream(reader);
		}
		
		return points;
	}
}