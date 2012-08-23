package mapreduce_bnl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import mapreduce_bnl.io.TupleWritable;

/***
 * To get skyline, this class help it,
 * read 'Skyline Operator' paper for detail.  
 * @author dke
 */
public class SkylineHelper {

	private int maximumWindowSize = 10000;	
	private ArrayList<TupleWritable> yetComparableTuples;	
	
	public SkylineHelper() {
		
	}
	
	public SkylineHelper(Iterator<TupleWritable> tuples) {
		this.setTuples(tuples);	
	}	

	public void setMaximumWindowSize(int maximumWindowSize) {
		this.maximumWindowSize = maximumWindowSize;		
	}	
	
	public void setTuples(Iterator<TupleWritable> iterator) {
		this.yetComparableTuples = new ArrayList<TupleWritable>();		
		
		while(iterator.hasNext()) {
			TupleWritable tuple = iterator.next();
			this.yetComparableTuples.add(new TupleWritable(tuple.getX(), tuple.getY()));
		}	
	}
	
	public ArrayList<TupleWritable> getSkylineUsingBNL()
		throws IOException {
		ArrayList<TupleWritable> skyline = new ArrayList<TupleWritable>();
		
		while(this.yetComparableTuples.size() > 0) {
			ArrayList<TupleWritable> window = this.iterate();
			skyline.addAll(window);		
		}
		
		return skyline;
	}
	
	public ArrayList<TupleWritable> iterate() throws IOException {
		
		ArrayList<TupleWritable> window = new ArrayList<TupleWritable>(maximumWindowSize);
		ArrayList<TupleWritable> diskTuples = new ArrayList<TupleWritable>();
		
		
		for (TupleWritable tuple : this.yetComparableTuples) {
			
				ArrayList<TupleWritable> dominatedTuples;

				// case 1 : tuple is dominated by window.			
				if (tuple.dominatedByWindow(window)) {
					// do nothing. because tuple is eliminated.				
				}			
				else if ((dominatedTuples = tuple.dominatedTuples(window)).size() > 0) {
					// case 2 : tuple dominate on or more tuples in the window			
					window.removeAll(dominatedTuples);
				}
				else { 
					// case 3 : tuple is incomparable with all tuples in the window.

					if(window.size() < this.maximumWindowSize) {
						window.add(new TupleWritable(tuple.getX(), tuple.getY()));
					} else {
						// if window is full, write tuple to disk					
						diskTuples.add(new TupleWritable(tuple.getX(), tuple.getY()));
					}					
				}
			}
		
		this.yetComparableTuples = diskTuples;		
		return window;
	}

	private void writeToFile(TupleWritable tuple) throws IOException {
		String uri = "temp.txt";
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(uri);
		FSDataOutputStream out = fs.create(path);
		
		//write
		tuple.write(out);
		out.flush();		
		
		out.close();
	}
}
