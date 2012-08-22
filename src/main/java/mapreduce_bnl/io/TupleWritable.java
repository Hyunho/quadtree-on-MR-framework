package mapreduce_bnl.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class TupleWritable implements Writable  {
	
	private IntWritable x;
	private IntWritable y;
	
	public TupleWritable()	{
		set(new IntWritable(), new IntWritable());		
	}
	
	public TupleWritable(int x, int y) {
		this.set(new IntWritable(x), new IntWritable(y));
	}
	
	public TupleWritable(IntWritable x, IntWritable y) {
		this.set(x, y);
	}
	
	public void set(IntWritable x, IntWritable y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x.get();		
	}
	
	public int getY() {
		return this.y.get();		
	}
		
	@Override
	public void write(DataOutput out) throws IOException {		
		x.write(out);
		y.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		x.readFields(in);
		y.readFields(in);
	}

	@Override
	public int hashCode() {		
		return x.hashCode() * 163 + y.hashCode();
	}
	
	@Override 
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof TupleWritable)) return false;		                 
		TupleWritable other = (TupleWritable) o;
		return (this.x.get() == other.x.get())  && (this.y.get() == other.y.get());      
	}
	
	@Override
	public String toString() {
		return x.get() + "\t" + y.get();
	}
	
	/**
	 * check that this tuple dominates other tuple.
	 * @param other
	 * @return
	 */	
	public boolean dominate(TupleWritable other) {
		
		boolean condition1 = (this.x.get() <= other.x.get() && this.y.get() <= other.y.get());
		boolean condition2 = (this.x.get() < other.x.get() || this.y.get() < other.y.get()); 
		return (condition1 && condition2);
	}	
	
	/**
	 * check that this tuple is dominated by all tuples in window.
	 * @param tuple
	 * @param window
	 * @return
	 */
	public boolean dominatedByWindow(ArrayList<TupleWritable> window) {
		
		boolean dominatedByWindow = false;
		
		for(TupleWritable q: window) {
			if(q.dominate(this))
				dominatedByWindow = true;
		}
		return dominatedByWindow;
	}
	

	/**
	 * return dominated tuples in window by tuple. 
	 * @param window
	 * @return dominated tuples
	 */
	public ArrayList<TupleWritable> dominatedTuples(ArrayList<TupleWritable> window) {

		ArrayList<TupleWritable> dominatedTupels = new ArrayList<TupleWritable>();			
		//check tuples in window dominate a point
		for (TupleWritable q : window) {				
			if(this.dominate(q))
				dominatedTupels.add(q);
		}
		return dominatedTupels;
	}
	
}
