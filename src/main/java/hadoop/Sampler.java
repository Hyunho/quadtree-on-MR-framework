package hadoop;


import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;

import hadoop.io.PointInputStream;
import index.quadtree.Point;

public class Sampler {
	
	public static Point[] reservoirSampling(String filename, int numSample, int dimension) throws IOException {
		
		Point[] points = new Point[numSample];
		Configuration conf = new Configuration();
		
		FileSystem fs = FileSystem.get(conf);
		
		PointInputStream dis = new PointInputStream(fs.open(new Path(filename)), dimension);		
		SequenceFile.Reader reader = null;
		try {
			int index = 0;
			Random rand = new Random();
			
			int numByteOfLine = dis.numByteOfLine();

			int numToSkip =0;

			while(true) {
				if(index< numSample) {							
					Point point = dis.readPoint();						
					points[index] = point;
				}else {
					int r = rand.nextInt(index);
					if(r < numSample) {
						dis.skipBytes(numToSkip);	
						Point point = dis.readPoint();	
						if(point ==null)
							break;
						points[r] = point;		
						numToSkip = 0;
					}
					numToSkip += numByteOfLine; 
				}
				index++;				
			}

		}finally {
			IOUtils.closeStream(reader);
		}
		return points;
	}
}