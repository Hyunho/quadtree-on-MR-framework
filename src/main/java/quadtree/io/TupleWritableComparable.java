package quadtree.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class TupleWritableComparable implements WritableComparable<TupleWritableComparable> {

	private int airTemperature;
	private int atmosphericPressure;
	
	public TupleWritableComparable(int airTemperature, int atmosphericPressure)
	{
		this.airTemperature = airTemperature;
		this.atmosphericPressure = atmosphericPressure;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(airTemperature);
		out.writeInt(atmosphericPressure);
	}

	@Override	
	public void readFields(DataInput in) throws IOException {
		airTemperature = in.readInt();
		atmosphericPressure = in.readInt();
	}


	@Override
	public int compareTo(TupleWritableComparable o) {
		int thisValue = this.airTemperature;
		int thatValue = o.airTemperature;
		return (thisValue < thatValue ? -1 : (thisValue==thatValue ? 0 : 1));
	}
	
	@Override
	public boolean equals(Object o) 
	{
		if (this == o) return true;
		
		if (!(o instanceof TupleWritableComparable)) return false;		
		TupleWritableComparable other = (TupleWritableComparable) o;
		return this.compareTo(other) == 0;		
	}

	public int getAirTemperature() {
		return this.airTemperature;
	}

	public int getAtmosphericPressure() {
		return this.atmosphericPressure;
	}

}
