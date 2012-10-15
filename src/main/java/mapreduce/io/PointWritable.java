package mapreduce.io;

import index.quadtree.Point;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.*;



public class PointWritable implements Writable {
	
	private Point point;
	public Point point() { return this.point; }
	
	
	class DoubleArrayWritable extends ArrayWritable{
		public DoubleArrayWritable() {
			super(DoubleWritable.class);
		}
	}
	
	public PointWritable() {
		this.set(new Point());
	}
	public PointWritable(Point point) {
		this.set(point);
	}
	
	public void set(Point point) {
		this.point = point;		
	}
	
	

	@Override
	public void write(DataOutput out) throws IOException {

		DoubleArrayWritable law = new DoubleArrayWritable();		
		
		double[] values = point.values();
					
		DoubleWritable[] lw  = new DoubleWritable[values.length];
		
		for(int i=0; i< values.length ; i++) {
			lw[i] = (new DoubleWritable(values[i]));
		}
		
		law.set(lw);		
		law.write(out);
	}

	@Override	
	public void readFields(DataInput in) throws IOException {
		
		DoubleArrayWritable law = new DoubleArrayWritable();
		
		law.readFields(in);
		
		Writable[] w = law.get();
		
		double[] values = new double[w.length];
		
		for(int i=0; i< w.length ; i++) {
			values[i] = ((DoubleWritable)w[i]).get();
		}
		
		this.set(new Point(values));
	}
	
	@Override
	public boolean equals(Object o) 
	{		
		if (this == o) return true;
		
		if (!(o instanceof PointWritable)) return false;		
		PointWritable other = (PointWritable) o;
		return this.point.equals(other.point);		
	}
	
	@Override
	public String toString() {
		return this.point.toString();
	}
}
