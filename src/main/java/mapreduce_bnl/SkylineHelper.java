package mapreduce_bnl;

import java.util.ArrayList;
import java.util.Iterator;

import mapreduce_bnl.io.TupleWritable;

public class SkylineHelper {

	private int windowSize = 10000;
	
	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;		
	}	

	public ArrayList<TupleWritable> getSkylineUsingBNL(
			Iterator<TupleWritable> tuples) {
		/**
		 * read 'Skyline Operator' paper to see detail. 
		 */
		
		ArrayList<TupleWritable> window = new ArrayList<TupleWritable>(windowSize);		
		 
		
		while (tuples.hasNext()){		

			TupleWritable tuple = tuples.next();
			
			ArrayList<TupleWritable> dominatedTuples;
			
			// case 1 : tuple is dominated by window.			
			if (tuple.dominatedByWindow(window)) {
				// do nothing. because tuple is eliminated.				
			}			
			else if ((dominatedTuples = tuple.dominatedTuples(window)).size() > 0) {
				// case 2 : tuple dominate on or more tuples in the window			
				window.removeAll(dominatedTuples);
			}
			else { // case 3 : tuple is incomparable with all tuples in the window.
				
				if(window.size() < this.windowSize) {
					window.add(new TupleWritable(tuple.getX(), tuple.getY()));
				} else { // if window 
					
				}
					// write tuple to disk					
			}
		}
		
		return window;
	}
	
	/**
	 * check that a tuple is dominated by all tuples in window.
	 * @param tuple
	 * @param window
	 * @return
	 */
	private boolean dominatedByWindow(TupleWritable tuple, ArrayList<TupleWritable> window) {

		boolean dominatedByWindow = false;
		
		for(TupleWritable q: window) {
			if(q.dominate(tuple))
				dominatedByWindow = true;
		}
		return dominatedByWindow;
	}
	

	
}
