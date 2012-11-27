package hadoop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import index.quadtree.Point;

public class Sampler {
	
	
	public static Point[] reservoirSampling(String filename, int numSample) throws IOException {
		
		Point[] points = new Point[numSample];
		Configuration conf = new Configuration();
		
		FileSystem fs = FileSystem.get(URI.create(filename), conf);
		
		DataInputStream dfs = new DataInputStream(fs.open(new Path(filename)));
		BufferedReader reader = new BufferedReader(new InputStreamReader(dfs));
		
		String line;
		int index = 0;
		
		Random rand = new Random();

		while ((line = reader.readLine()) != null){

			if ( index < numSample ) {
				points[index] = Point.stringToPoint(line);
			}else {
				int r = rand.nextInt(index);
				if(r < numSample)
					points[r] = Point.stringToPoint(line);
			}
			index++; 
		}
		reader.close();
		return points;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		String filename= args[0];
		int numSample = Integer.parseInt(args[1]);
		String output = args[2];
		
		try {
			Point[] sample = Sampler.reservoirSampling(filename, numSample);
			
			try {				
				PrintWriter out
				= new PrintWriter(
						new BufferedWriter(
								new FileWriter(output, true)));
				
				for(int i=0; i < numSample; i++) {
					out.write(sample[i].toString());	
				}

				out.println();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
