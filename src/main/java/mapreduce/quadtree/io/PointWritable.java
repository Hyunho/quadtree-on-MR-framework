package mapreduce.quadtree.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.*;


import quadtree.Point;

public class PointWritable implements Writable {
	
	private Point point;
	public Point point() { return this.point; }
	
	
	class LongArrayWritable extends ArrayWritable{
		public LongArrayWritable() {
			super(LongWritable.class);
		}
	}
	
	public PointWritable(Point point) {	
		this.point = point;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		LongArrayWritable law = new LongArrayWritable();		
		
		double[] values = point.values();
		
		LongWritable[] lw  = new LongWritable[values.length];
		
		for(int i=0; i< values.length ; i++) {
			lw[i].set((long) values[i]);
		}
		
		law.set(lw);
	}

	@Override	
	public void readFields(DataInput in) throws IOException {
		LongArrayWritable law = new LongArrayWritable();
		law.readFields(in);
		
		LongWritable[] la = (LongWritable[]) law.get();
		
		double[] values = new double[la.length];
		
		for(int i=0; i< la.length ; i++) {
			values[i] = la[i].get();
		}
	}
	
	@Override
	public boolean equals(Object o) 
	{		
		if (this == o) return true;
		
		if (!(o instanceof PointWritable)) return false;		
		PointWritable other = (PointWritable) o;
		return this.point.equals(other.point);		
	}
}
