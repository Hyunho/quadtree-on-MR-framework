package mapreduce.bnl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import mapreduce.bnl.io.TupleFile;
import mapreduce.bnl.io.TupleWritable;

/***
 * To get skyline, this class help it,
 * read 'Skyline Operator' paper for detail.  
 * @author dke
 */
public class SkylineComputer {

	
	class TupleWithTimestamp {
		TupleWritable tuple;
		int timestamp;
	}

	private Iterator<TupleWritable> input;
	private int maximumWindowSize;
	
	private String temporaryFilename = "temp.dat";
	private String skylineFilename = "skyline.dat";
	
	public SkylineComputer(Iterator<TupleWritable> input) {
		this.input = input; 
	}

	/**
	 * get skyline using BNL procedure.
	 * @return
	 * @throws IOException 
	 */
	public Iterator<TupleWritable> SkylineBNL() throws IOException {
		
		//initialization		
		ArrayList<TupleWithTimestamp> window = new ArrayList<TupleWithTimestamp>();		
		TupleFile skylineFile = new TupleFile(skylineFilename);
		TupleFile temporaryFile = new TupleFile(temporaryFilename);		
		int countIn = 0;
		int countOut = 0;
		
		// Scanning the database repeatedly
		while (input.hasNext()) {
						
			Iterator<TupleWithTimestamp> windowIterator = window.iterator();
			
			while(windowIterator.hasNext()) {
				TupleWithTimestamp p = windowIterator.next();
					
				if (p.timestamp == countIn ) {
					skylineFile.insertTuple(p.tuple);
					windowIterator.remove();
				}
			}
			
			//load next point			
			TupleWithTimestamp p = new TupleWithTimestamp();
			p.tuple = input.next();			
			
			p.timestamp = countOut;
			
			// compare it to all points in window
			boolean dominated = false;
			
			windowIterator = window.iterator();
			
			while(windowIterator.hasNext()) {
				TupleWithTimestamp q = windowIterator.next();
				
				if (q.tuple.dominate(p.tuple)) {
					dominated = true;
					break;
				}
				if (p.tuple.dominate(q.tuple)) {
					windowIterator.remove();
				}
			}
			
			if (!dominated) {				
				//check available memory.
				if(window.size() < this.maximumWindowSize)	{					
					window.add(p);
				}
				else {
					temporaryFile.insertTuple(p.tuple);
					countOut = countOut +1;
				}
			}
			
			if (!input.hasNext()) {	
				temporaryFile.close();				
				this.input = TupleFile.iterator(temporaryFilename);
				
				countIn = 0;
				countOut = 0;				
			}
				
		}
		
		skylineFile.close();
		
		//TupleFile.delete(temporaryFilename);		
		return TupleFile.iterator(skylineFilename);
	}

	public void setMaximumWindowSize(int size) {
		this.maximumWindowSize = size;
	}
}
