package mapreduce_bnl.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class TupleWritable implements Writable  {
	
	private IntWritable x;
	private IntWritable y;
	
	public TupleWritable(int x, int y)
	{
		this.x = new IntWritable(x);
		this.y = new IntWritable(y);
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
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof TupleWritable)) return false;		                 
		TupleWritable other = (TupleWritable) o;
		return (this.x.get() == other.x.get())  && (this.y.get() == other.y.get());      
	}
	
	@Override
	public String toString() {
		return "x : " + x.get() + ", y : " + y.get();
	}
	
	
	public boolean dominate(TupleWritable other) {
		
		boolean condition1 = (this.x.get() <= other.x.get() && this.y.get() <= other.y.get());
		boolean condition2 = (this.x.get() < other.x.get() || this.y.get() < other.y.get()); 
		return (condition1 && condition2);
	}
	
}
