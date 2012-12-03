package hadoop;

import index.quadtree.Point;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class StringToBinaryConverter {
	
	
	public static void main(String[] args) throws IOException {
		
		String inputUri = args[0];
		String outputUri = args[1];
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		
		DataInputStream dis = new DataInputStream(fs.open(new Path(inputUri)));		
		DataOutputStream os = fs.create(new Path(outputUri));
		
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(dis));

			String line;
			while ((line = reader.readLine()) != null){
				
				Point point = Point.stringToPoint(line);
				
				int size = point.dimension();
				for(int i=0; i<size; i++) {
					os.writeDouble(point.get(i));
				}
//				System.out.println(point.get(0));
//				os.writeDouble(point.get(0));
//				os.write
//				os.writeDouble(908.644929993898d);
//				}
				os.writeChar('\n');
			}
		} finally{
			os.close();
			dis.close();
		}
	}
}
