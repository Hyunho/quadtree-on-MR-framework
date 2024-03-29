package mapreduce.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * This class is used in Map task in merging job.
 * @author dke
 *
 */
public class FlagWritable implements Writable {


	private Text flag;
	private TupleWritable tuple;	
	
	public Text getFlag() {return this.flag;}
	public TupleWritable getTuple() {	return this.tuple; }
	
	
	public FlagWritable(){
		this.set(new Text(), new TupleWritable());
	}	
	
	public FlagWritable(Text flag, TupleWritable tuple)	{
		this.flag = flag;
		this.tuple = tuple;		
	}
	
	public void set(Text flag, TupleWritable tuple) {
		this.flag = flag;
		this.tuple = tuple;		
	}
	
		
	@Override
	public void write(DataOutput out) throws IOException {
		this.flag.write(out);
		this.tuple.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.flag.readFields(in);
		this.tuple.readFields(in);		
	}	
	

	@Override 
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof FlagWritable)) return false;		                 
		FlagWritable other = (FlagWritable) o;
		return (this.flag.equals(other.flag) &&
				this.tuple.equals(other.tuple));		      
	}	
	
	@Override
	public String toString() {
		return flag + "\t" + tuple.toString();
	}
	
	
}
