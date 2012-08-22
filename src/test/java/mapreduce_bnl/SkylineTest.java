package mapreduce_bnl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import mapreduce_bnl.io.TupleWritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.junit.*;

public class SkylineTest {

	
	@Test
	public void blockNestedLoop() {	
		
		Iterator<TupleWritable> values = Arrays.asList(
				new TupleWritable(10, 10),
				new TupleWritable(20, 20),
				new TupleWritable(5, 15),
				new TupleWritable(15, 5),
				new TupleWritable(10, 30)).iterator();
		
		SkylineHelper helper = new SkylineHelper();
		
		//helper.setWindowSize(1);		
		ArrayList<TupleWritable> skyline = helper.getSkylineUsingBNL(values);
		
		assertTrue(skyline.contains(new TupleWritable(10, 10)));
		assertTrue(skyline.contains(new TupleWritable(5, 15)));
		assertTrue(skyline.contains(new TupleWritable(10, 10)));
		assertFalse(skyline.contains(new TupleWritable(10, 30)));
		assertEquals(3, skyline.size());
	}
	
	@Test
	public void inputAndOuputToLocalFileSystem() throws IOException {
		 
		
		String uri = "temp.txt";
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(uri);
		FSDataOutputStream out = fs.create(path);
		

		IntWritable origin_value = new IntWritable(10);
		
		//write
		origin_value.write(out);
		out.flush();
		out.close();
		
		FSDataInputStream input = fs.open(path);
		
		IntWritable read_value = new  IntWritable();
		read_value.readFields(input);
		
		//out.close();
		assertTrue(fs.exists(path));
		assertEquals(origin_value, read_value);		
	}
}



