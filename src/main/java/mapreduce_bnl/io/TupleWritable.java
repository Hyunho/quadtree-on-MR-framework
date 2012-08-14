package mapreduce_bnl.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import quadtree.io.TupleWritableComparable;


public class TupleWritable implements Writable  {
	
	private int x;
	private int y;
	
	public TupleWritable(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
		
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(x);
		out.writeInt(y);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		x = in.readInt();
		y = in.readInt();
	}

	@Override
	public boolean equals(Object o) 
	{
		if (this == o) return true;
		if (!(o instanceof TupleWritable)) return false;
		
		TupleWritable other = (TupleWritable) o;
		return (this.x == other.x)  && (this.y == other.y);		
	}
}
