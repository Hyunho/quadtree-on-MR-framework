package mapreduce_bnl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import mapreduce.bnl.io.TupleFile;
import mapreduce.bnl.io.TupleIterator;
import mapreduce.bnl.io.TupleWritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.junit.*;

public class SkylineTest {

	private ArrayList<TupleWritable> tuples;
	
	@Before
	public void setUp() throws Exception {
		
		this.tuples = new ArrayList<TupleWritable>(
				Arrays.asList(
						new TupleWritable(10, 10),
						new TupleWritable(20, 20),
						new TupleWritable(5, 15),
						new TupleWritable(15, 5),
						new TupleWritable(10, 30)));
	}
	
	
	//@Test
	public void getSkylineUsingBNL() throws IOException {
		
//		SkylineComputer helper = new SkylineComputer(this.tuples.iterator());
//		
//		helper.setMaximumWindowSize(2);
//		ArrayList<TupleWritable> skyline = helper.getSkylineUsingBNL();
//		
//		assertEquals(3, skyline.size());
//		
// 		assertTrue(skyline.contains(new TupleWritable(10, 10)));
//		assertTrue(skyline.contains(new TupleWritable(5, 15)));
//		assertTrue(skyline.contains(new TupleWritable(10, 10)));
//		assertFalse(skyline.contains(new TupleWritable(10, 30)));		
	}
	
	//@Test
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
	
	@Test
	public void dominationTest() throws IOException {
		TupleWritable tuple1 = new TupleWritable(10, 10);
		TupleWritable tuple2 = new TupleWritable(20, 20);
		
		assertTrue(tuple1.dominate(tuple2));
		assertFalse(tuple2.dominate(tuple1));
	}
	
	@Test	
	public void createTupleFile() throws IOException {

//		String fileName = new String("filename");
//		
//		TupleFile file = new TupleFile(fileName);
//		
//		TupleWritable tuple = new TupleWritable(50, 50);
//		file.insertTuple(tuple);
//		file.insertTuple(tuple);
//		file.insertTuple(tuple);
//		
//		file.close();
//		
//		TupleIterator iterator =  TupleFile.tupleIterator(fileName);
//		
//		assertEquals(tuple , iterator.next());
//		assertEquals(tuple , iterator.next());
//		assertEquals(tuple , iterator.next());
//		
//		assertFalse(iterator.hasNext());
//		
//		TupleFile.delete(fileName);
	}
	
}



